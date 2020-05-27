package com.fimet.server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import com.fimet.FimetException;
import com.fimet.FimetLogger;
import com.fimet.entity.ESocketStore;
import com.fimet.socket.ISocket;
import com.fimet.socket.ISocketStore;
import com.fimet.utils.JsonUtils;
import com.fimet.utils.converter.Converter;

public class SocketStore implements ISocketStore {
	File STORE_PATH = new File("store/task");
	FileWriter store;
	boolean enable = true;
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

}
