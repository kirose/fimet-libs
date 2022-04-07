package com.fimet.stress.creator;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.LoggerFactory;import org.slf4j.Logger;

import java.util.Set;

import com.fimet.FimetException;

import com.fimet.parser.IMessage;
import com.fimet.parser.IStreamAdapter;
import com.fimet.parser.Message;
import com.fimet.simulator.ISimulatorModel;
import com.fimet.stress.IFileCreator;
import com.fimet.utils.MessageUtils;


public class CartesianCreator implements IFileCreator {
	private static Logger logger = LoggerFactory.getLogger(CartesianCreator.class);
	private IMessage message;
	private File output;
	private Map<String, String[]> variationParameters = new HashMap<>();
	private boolean append;
	private IVariator variator = DefaultVariator.INSTANCE;
	private IStreamAdapter adapter;
	private ISimulatorModel simulatorModel;
	public CartesianCreator(String outputPath) {
		this(new File(outputPath), null, false);
	}
	public CartesianCreator(File output) {
		this(output, null, false);
	}
	public CartesianCreator(File output, Message message) {
		this(output, message, false);
	}
	public CartesianCreator(File output, IMessage message, boolean append) {
		this.message = message;
		this.output = output;
		this.append = append;
	}
	public CartesianCreator setMessage(IMessage message) {
		this.message = message;
		if (adapter == null && message.hasProperty(IMessage.ADAPTER)) {
			adapter = (IStreamAdapter)message.getProperty(IMessage.ADAPTER);
		}
		return this;
	}
	public CartesianCreator adapter(IStreamAdapter adapter) {
		this.adapter = adapter;
		return this;
	}
	public CartesianCreator variations(String idField, String... variations) {
		variationParameters.put(idField, variations);
		return this;
	}
	public CartesianCreator variations(Map<String, String[]> variations) {
		variationParameters = variations;
		return this;
	}
	public CartesianCreator variator(IVariator variator) {
		this.variator = variator != null ? variator : DefaultVariator.INSTANCE;
		return this;
	}
	public CartesianCreator simulatorModel(ISimulatorModel simulatorModel) {
		this.simulatorModel = simulatorModel;
		return this;
	}
	@Override
	public File create() {
		FileOutputStream writer = null;
		if (adapter == null) {
			throw new FimetException("Adapter cannot be null");
		} else {
			message.setProperty(IMessage.ADAPTER, adapter);
		}
		try {
			writer = new FileOutputStream(output,append);
			CartesianIterator iterator = new CartesianIterator(variationParameters);
			FieldVariation[] next;
			StringBuilder errors = new StringBuilder();
			while (iterator.hasNext()) {
				next = iterator.next();
				try {
					message = variator.variate(message, next);
					message = simulatorModel.simulateRequest(message);
					writer.write(MessageUtils.format(message));
					writer.write((byte)10);
				} catch (Exception e){
					if (errors.length()<10000)
						errors.append(e.getMessage()).append('\n');
				}
			}
			if (errors.length() != 0) {
				logger.error("Error formating message: "+errors.toString());	
			}
		} catch (Exception e) {
			logger.error("Creator Error",e);
		} finally {
			if (writer != null) {
				try {writer.close();} catch(Exception e) {}
			}
		}
		return output;
	}
	private class CartesianIterator {
		String[][] data;
		FieldVariation[] columns;
		int curr;
		int size;
		
		CartesianIterator(Map<String, String[]> parameters){
			Set<Entry<String, String[]>> set = parameters.entrySet();
			data = new String[set.size()][];
			columns = new FieldVariation[set.size()];
			int i = 0;
			size = 1;
			for (Entry<String, String[]> e : set) {
				columns[i] = new FieldVariation();
				columns[i].idField = e.getKey();
				data[i] = e.getValue();
				columns[i].value = data[i][0];
				size *= e.getValue().length;
				i++;
			}
			columns[columns.length-1].index = -1;
			curr = -1;
		}
		boolean hasNext() {
			return curr + 1 < size;
		}
		FieldVariation[] next() {
			curr++;
			for (int i = columns.length-1; i >= 0; i--) {
				if (columns[i].index+1 < data[i].length) {
					columns[i].index++;
					columns[i].value = data[i][columns[i].index];
					break;
				} else {
					columns[i].index = 0;
					columns[i].value = data[i][0];
				}
			}
			return columns;
		}
	}
}
