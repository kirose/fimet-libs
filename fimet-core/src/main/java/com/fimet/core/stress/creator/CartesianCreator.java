package com.fimet.core.stress.creator;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.fimet.commons.FimetLogger;
import com.fimet.core.iso8583.parser.Message;
import com.fimet.core.parser.MessageUtils;
import com.fimet.core.stress.IFileCreator;


public class CartesianCreator implements IFileCreator {
	private Message message;
	private File output;
	private Map<String, String[]> variationParameters = new HashMap<>();
	private boolean append;
	private IVariator variator = DefaultVariator.INSTANCE;
	public CartesianCreator(String outputPath) {
		this(new File(outputPath), null, false);
	}
	public CartesianCreator(File output) {
		this(output, null, false);
	}
	public CartesianCreator(File output, Message message) {
		this(output, message, false);
	}
	public CartesianCreator(File output, Message message, boolean append) {
		this.message = message;
		this.output = output;
		this.append = append;
	}
	public CartesianCreator setMessage(Message message) {
		this.message = message;
		return this;
	}
	public CartesianCreator addVariation(String idField, String[] variations) {
		variationParameters.put(idField, variations);
		return this;
	}
	public CartesianCreator setVariator(IVariator variator) {
		this.variator = variator != null ? variator : DefaultVariator.INSTANCE;
		return this;
	}
	@Override
	public File create() {
		FileOutputStream writer = null;
		try {
			writer = new FileOutputStream(output,append);
			CartesianIterator iterator = new CartesianIterator(variationParameters);
			FieldVariation[] next;
			StringBuilder errors = new StringBuilder();
			while (iterator.hasNext()) {
				next = iterator.next();
				try {
					message = variator.variate(message, next);
					writer.write(MessageUtils.formatMessage(message));
					//writer.write((byte)10);
				} catch (Exception e){
					errors.append(e.getMessage()).append('\n');
				}
			}
			if (errors.length() != 0) {
				FimetLogger.error(CartesianCreator.class,"Error formating message: "+errors.toString());	
			}
		} catch (Exception e) {
			FimetLogger.error(CartesianCreator.class,"Creator Error",e);
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
