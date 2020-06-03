package com.fimet.cmd;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import com.fimet.FimetException;
import com.fimet.FimetLogger;
import com.fimet.parser.IMessage;
import com.fimet.simulator.ISimulator;
import com.fimet.simulator.ISimulatorStore;
import com.fimet.utils.JsonUtils;

public class SimulatorStore extends SocketStore implements ISimulatorStore {
	File STORE_PATH = new File("store/task");
	FileWriter store;
	public SimulatorStore() {
	}
	@Override
	public void storeIncoming(ISimulator simulator, IMessage message, byte[] bytes) {
		save('I', simulator, message, bytes);
	}
	@Override
	public void storeOutgoing(ISimulator simulator, IMessage message, byte[] bytes) {
		save('O', simulator, message, bytes);
	}
	private void save(char type, ISimulator simulator, IMessage message, byte[] bytes) {
		try {
			ESimulatorStore e = new ESimulatorStore();
			e.setAddress(simulator.getSocket().getAddress());
			e.setPort(simulator.getSocket().getPort());
			e.setModel(simulator.getModel().getName());
			e.setTime(System.currentTimeMillis());
			e.setType(type);
			e.setMessage(message);
			String line = JsonUtils.toJson(e)+"\n";
			synchronized (this) {
				store.write(line);
			}
			store.flush();
		} catch (Throwable ex) {
			FimetLogger.error(getClass(), ex);
		}
	}
	@Override
	public void open(UUID idTask) {
		super.open(idTask);
		if (store != null) {
			throw new FimetException("Unable to open, you must call close before this operation");
		}
		try {
			store = new java.io.FileWriter(new File(STORE_PATH, ""+idTask+"/simulator.json"));
		} catch (IOException e) {
			throw new FimetException(e);
		}
	}
	@Override
	public void close(UUID idTask) {
		super.close(idTask);
		if (store !=null) {
			try {store.close();} catch(Throwable e) {}
			store = null;
		}
	}
	public class ESimulatorStore {
		private String address;
		private Integer port;
		private String model;
		private Long time;
		private Character type;
		private IMessage message;
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
		public Character getType() {
			return type;
		}
		public void setType(Character type) {
			this.type = type;
		}
	}
}
