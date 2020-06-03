package com.fimet.cmd;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import com.fimet.FimetException;
import com.fimet.FimetLogger;
import com.fimet.socket.ISocket;
import com.fimet.socket.ISocketStore;
import com.fimet.utils.JsonUtils;
import com.fimet.utils.converter.Converter;

public class SocketStore implements ISocketStore {
	File STORE_PATH = new File("store/task");
	FileWriter store;
	public SocketStore() {
	}
	@Override
	public void storeIncoming(ISocket socket, byte[] message) {
		save('I', socket, message);
	}
	@Override
	public void storeOutgoing(ISocket socket, byte[] message) {
		save('O', socket, message);
	}
	public void save(char type, ISocket socket, byte[] message) {
		try {
			ESocketStore e = new ESocketStore();
			e.setAddress(socket.getAddress());
			e.setPort(socket.getPort());
			e.setType(type);
			e.setTime(System.currentTimeMillis());
			e.setMessage(new String(Converter.asciiToHex(message)));
			String line = JsonUtils.toJson(e)+"\n";
			synchronized (this) {
				store.write(line);
			}
			store.flush();
		} catch (Throwable ex) {
			FimetLogger.error(getClass(), ex);
		}
	}
	public void open(UUID idTask) {
		if (store != null) {
			throw new FimetException("Unable to open, you must call close before this operation");
		}
		try {
			new File(STORE_PATH, ""+idTask).mkdirs();
			File file = new File(STORE_PATH, ""+idTask+"/socket.json");
			store = new java.io.FileWriter(file);
		} catch (IOException e) {
			throw new FimetException(e);
		}
	}
	public void close(UUID idTask) {
		if (store !=null) {
			try {store.close();} catch(Throwable e) {}
			store = null;
		}
	}
	public class ESocketStore {
		private String address;
		private Integer port;
		private Long time;
		private Character type;
		private String message;
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
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
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
