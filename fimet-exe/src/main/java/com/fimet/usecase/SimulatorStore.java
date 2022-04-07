package com.fimet.usecase;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.LoggerFactory;import org.slf4j.Logger;

import com.fimet.FimetException;

import com.fimet.exe.Task;
import com.fimet.parser.IMessage;
import com.fimet.simulator.ISimulator;
import com.fimet.simulator.ISimulatorStore;
import com.fimet.utils.JsonUtils;

public class SimulatorStore extends SocketStore implements ISimulatorStore {
	private static Logger logger = LoggerFactory.getLogger(SimulatorStore.class);
	private FileWriter writer;
	private static final String IN = "IN";
	private static final String OUT = "OUT";
	public SimulatorStore() {
	}
	public void init(Task task, Object... params) {
		super.init(task, params);
		try {
			writer = new java.io.FileWriter(new File(task.getFolder(),"simulator.json"));
		} catch (IOException e) {
			throw new FimetException(e);
		}		
	}
	@Override
	public void storeIncoming(ISimulator simulator, IMessage message, byte[] bytes) {
		save(IN, simulator, message, bytes);
	}
	@Override
	public void storeOutgoing(ISimulator simulator, IMessage message, byte[] bytes) {
		save(OUT, simulator, message, bytes);
	}
	private void save(String type, ISimulator simulator, IMessage message, byte[] bytes) {
		try {
			ESimulatorStore e = new ESimulatorStore();
			e.setName(simulator.getName());
			e.setAddress(simulator.getSocket().getAddress());
			e.setPort(simulator.getSocket().getPort());
			e.setModel(simulator.getModel().getName());
			e.setTime(System.currentTimeMillis());
			e.setType(type);
			e.setMessage(message);
			String line = JsonUtils.toJson(e)+"\n";
			synchronized (this) {
				writer.write(line);
			}
			writer.flush();
		} catch (Throwable ex) {
			logger.error("Stores simulator exception", ex);
		}
	}
	public void close() {
		super.close();
		if (writer !=null) {
			try {writer.close();} catch(Throwable e) {}
			writer = null;
		}
	}
	public class ESimulatorStore {
		private String name;
		private String address;
		private Integer port;
		private String model;
		private Long time;
		private String type;
		private IMessage message;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public Integer getPort() {
			return port;
		}
		public void setPort(Integer port) {
			this.port = port;
		}
		public String getModel() {
			return model;
		}
		public void setModel(String model) {
			this.model = model;
		}
		public IMessage getMessage() {
			return message;
		}
		public void setMessage(IMessage message) {
			this.message = message;
		}
		public Long getTime() {
			return time;
		}
		public void setTime(Long time) {
			this.time = time;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
	}
}
