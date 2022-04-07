package com.fimet.stress.creator;

import java.io.File;
import java.io.FileOutputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.LoggerFactory;import org.slf4j.Logger;

import com.fimet.FimetException;
import com.fimet.json.JField;
import com.fimet.json.JVariator;
import com.fimet.parser.IMessage;
import com.fimet.parser.IStreamAdapter;
import com.fimet.parser.Message;
import com.fimet.simulator.ISimulatorModel;
import com.fimet.stress.IFileCreator;
import com.fimet.utils.MessageUtils;
import com.fimet.utils.VariatorUtils;


public class VariatorCreator implements IFileCreator {
	private static Logger logger = LoggerFactory.getLogger(VariatorCreator.class);
	private IMessage message;
	private File output;
	private boolean append;
	private IStreamAdapter adapter;
	private ISimulatorModel simulatorModel;
	private JVariator variator;
	public VariatorCreator(String outputPath) {
		this(new File(outputPath), null, false);
	}
	public VariatorCreator(File output) {
		this(output, null, false);
	}
	public VariatorCreator(File output, Message message) {
		this(output, message, false);
	}
	public VariatorCreator(File output, IMessage message, boolean append) {
		this.message = message;
		this.output = output;
		this.append = append;
	}
	public VariatorCreator setMessage(IMessage message) {
		this.message = message;
		if (adapter == null && message.hasProperty(IMessage.ADAPTER)) {
			adapter = (IStreamAdapter)message.getProperty(IMessage.ADAPTER);
		}
		return this;
	}
	public VariatorCreator adapter(IStreamAdapter adapter) {
		this.adapter = adapter;
		return this;
	}
	public VariatorCreator simulatorModel(ISimulatorModel simulatorModel) {
		this.simulatorModel = simulatorModel;
		return this;
	}
	public VariatorCreator variator(JVariator variator) {
		this.variator = variator;
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
		List<Field> fields = new ArrayList<Field>(variator.getFields().size());
		for (JField f : variator.getFields()) {
			Field field;
			if ("sequential".equalsIgnoreCase(f.getType())) {
				field = new FieldSequential(f);
			} else if ("random".equalsIgnoreCase(f.getType())) {
				field = new FieldRandom(f);
			} else {
				throw new RuntimeException("Invalid type for field "+f.getId()+"-"+f.getValue());
			}
			fields.add(field);
		}
		try {
			writer = new FileOutputStream(output,append);
			StringBuilder errors = new StringBuilder();
			for (int i = variator.getStart(); i < variator.getEnd(); i++) {
				try {
					for (Field f : fields) {
						message.setValue(f.getId(), f.next(i));
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
	private static abstract class Field {
		protected String id;
		protected int length;
		protected char pad;
		protected char[] charset;
		protected String value;
		public Field(JField f) {
			this.id = f.getId();
			this.length = f.getLength();
			this.pad = f.getPad();
			this.value = f.getValue();
			this.charset = VariatorUtils.parseCharset(f.getCharset())
						.toCharArray();
		}
		public String getId() {
			return id;
		}
		abstract public String next(int seq);
	}
	private static class FieldRandom extends Field {
	    private Random random;
	    private char[] buf;
		public FieldRandom(JField f) {
			super(f);
	        this.buf = new char[length];
			random = new SecureRandom();
		}
		public String next(int seq) {
	        for (int idx = 0; idx < buf.length; ++idx) {
	            buf[idx] = charset[random.nextInt(charset.length)];
	        }
	        return this.value.replace("${v}", new String(buf));
		}
	}
	private static class FieldSequential extends Field {
		public FieldSequential(JField f) {
			super(f);
		}
		public String next(int seq) {
			char[] value = new char[length];
			int index;
			int module = charset.length;
			int i = length-1;
			do {
				index = (int)(seq%module);
				value[i--] = charset[index];
				seq = seq/module;
			} while (seq!=0 && i!=-1);
			for (; i > -1 ; i--) {
				value[i] = pad;	
			}
			return this.value.replace("${v}", new String(value));
		}
	}
	public static void main(String[] args) {
		sequential();
		//random();
	}
	public static void sequential() {
		JField jf = new JField();
		jf.setId("2");
		jf.setCharset("A-Z");
		jf.setLength(5);
		jf.setPad('0');
		jf.setValue("1234567890${v}6");
		Field f = new FieldSequential(jf);
		for (int i = 0; i < 100; i++) {
			System.out.println(f.next(i));
		}
	}
	public static void random() {
		JField jf = new JField();
		jf.setId("2");
		jf.setCharset("A-Z");
		jf.setLength(5);
		jf.setPad('0');
		jf.setValue("1234567890${v}6");
		Field f = new FieldRandom(jf);
		for (int i = 0; i < 100; i++) {
			System.out.println(f.next(i));
		}
	}
}
