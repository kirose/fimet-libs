package com.fimet.stress.creator;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;import org.slf4j.Logger;

import com.fimet.FimetException;

import com.fimet.parser.IMessage;
import com.fimet.parser.IStreamAdapter;
import com.fimet.parser.Message;
import com.fimet.simulator.ISimulatorModel;
import com.fimet.stress.IFileCreator;
import com.fimet.utils.MessageUtils;


public class ReplacementsCreator implements IFileCreator {
	private static Logger logger = LoggerFactory.getLogger(ReplacementsCreator.class);
	private IMessage message;
	private File output;
	private boolean append;
	private List<Map<String,String>> replacements;
	private IStreamAdapter adapter;
	private ISimulatorModel simulatorModel;
	public ReplacementsCreator(String outputPath) {
		this(new File(outputPath), null, false);
	}
	public ReplacementsCreator(File output) {
		this(output, null, false);
	}
	public ReplacementsCreator(File output, Message message) {
		this(output, message, false);
	}
	public ReplacementsCreator(File output, IMessage message, boolean append) {
		this.message = message;
		this.output = output;
		this.append = append;
	}
	public ReplacementsCreator setMessage(IMessage message) {
		this.message = message;
		if (adapter == null && message.hasProperty(IMessage.ADAPTER)) {
			adapter = (IStreamAdapter)message.getProperty(IMessage.ADAPTER);
		}
		return this;
	}
	public ReplacementsCreator adapter(IStreamAdapter adapter) {
		this.adapter = adapter;
		return this;
	}
	public ReplacementsCreator simulatorModel(ISimulatorModel simulatorModel) {
		this.simulatorModel = simulatorModel;
		return this;
	}
	public ReplacementsCreator replacements(List<Map<String,String>> replacements) {
		this.replacements = replacements;
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
			StringBuilder errors = new StringBuilder();
			for (Map<String,String> r : replacements) {
				try {
					for (Map.Entry<String, String> e : r.entrySet()) {
						message.setValue(e.getKey(), e.getValue());
					}
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
}
