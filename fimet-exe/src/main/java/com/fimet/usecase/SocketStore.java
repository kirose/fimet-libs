package com.fimet.usecase;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.LoggerFactory;import org.slf4j.Logger;

import com.fimet.FimetException;

import com.fimet.exe.Task;
import com.fimet.net.ISocket;
import com.fimet.net.ISocketStore;
import com.fimet.utils.JsonUtils;
import com.fimet.utils.converter.Converter;

public class SocketStore implements ISocketStore {
	private static Logger logger = LoggerFactory.getLogger(SocketStore.class);
	private static final String IN = "IN";
	private static final String OUT = "OUT";
	private FileWriter writer;
	public SocketStore() {
	}
	public void init(Task task, Object... params) {
		try {
			File file = new File(task.getFolder(), "socket.json");
			writer = new java.io.FileWriter(file);
		} catch (IOException e) {
			throw new FimetException(e);
		}		
	}
	@Override
	public void storeIncoming(ISocket socket, byte[] message) {
		save(IN, socket, message);
	}
	@Override
	public void storeOutgoing(ISocket socket, byte[] message) {
		save(OUT, socket, message);
	}
	public void save(String type, ISocket socket, byte[] message) {
		try {
			ESocketStore e = new ESocketStore();
			e.setName(socket.getName());
			e.setAddress(socket.getAddress());
			e.setPort(socket.getPort());
			e.setType(type);
			e.setTime(System.currentTimeMillis());
			e.setMessage(new String(Converter.asciiToHex(message)));
			String line = JsonUtils.toJson(e)+"\n";
			synchronized (this) {
				writer.write(line);
			}
			writer.flush();
		} catch (Throwable ex) {
			logger.error("Store socket exception", ex);
		}
	}
	public void close() {
		if (writer !=null) {
			try {writer.close();} catch(Throwable e) {}
			writer = null;
		}
	}
	public class ESocketStore {
		private String name;
		private String address;
		private Integer port;
		private Long time;
		private String type;
		private String message;
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
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
	}
}
