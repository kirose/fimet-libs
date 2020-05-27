package com.fimet.socket;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fimet.AbstractManager;
import com.fimet.ISocketManager;
import com.fimet.Manager;
import com.fimet.socket.ISocket;
import com.fimet.socket.ISocketListener;
import com.fimet.socket.ISocketMonitor;
import com.fimet.socket.ISocketStore;
import com.fimet.socket.PSocket;
import com.fimet.utils.ThreadUtils;

public class SocketManager extends AbstractManager implements ISocketManager {
	private Map<Integer, ISocket> connections = new ConcurrentHashMap<>();
	private List<ISocketMonitor> monitors = new ArrayList<>();
	private SocketStoreWrapper store;
	public SocketManager() {
		ISocketStore wrapped = Manager.getExtension(ISocketStore.class, NullSocketStore.INSTANCE);
		store = new SocketStoreWrapper(wrapped);
	}
	@Override
	public ISocket getSocket(PSocket socket, ISocketListener listener) {
		int hash = socket.hashCode();
		if (!connections.containsKey(hash)) {
			if (socket.isServer()) {
				AdaptedSocketServer socketServer = new AdaptedSocketServer(socket, listener);
				socketServer.setStore(store);
				connections.put(hash, socketServer);
			} else {
				AdaptedSocketClient socketClient = new AdaptedSocketClient(socket, listener);
				socketClient.setStore(store);
				connections.put(hash, socketClient);
			} 
		}
		return connections.get(hash);
	}
	@Override
	public ISocket getSocket(PSocket socket) {
		return getSocket(socket, null);
	}
	@Override
	public ISocket connect(PSocket socket, ISocketListener listener) {
		ISocket messenger = getSocket(socket, listener);
		messenger.connect();
		return messenger;
	}
	@Override
	public void disconnect(PSocket socket) {
		int hash = socket.hashCode();
		if (connections.containsKey(hash)) {
			connections.get(hash).disconnect();
		}
	}
	@Override
	public void disconnectAll() {
		for (Map.Entry<Integer, ISocket> e : connections.entrySet()) {
			e.getValue().disconnect();
		}
		connections.clear();
	}
	public void setSocketTimeReconnect(int sec) {
		AdaptedSocket.RECONNECTION_TIME = sec*1000;
	}
	@Override
	public void addMonitor(ISocketMonitor monitor) {
		monitors.add(monitor);
	}
	@Override
	public void removeMonitor(ISocketMonitor monitor) {
		monitors.remove(monitor);
	}
	void onSocketConnected(PSocket socket) {
		if (monitors != null && !monitors.isEmpty()) {
			ThreadUtils.runAsync(()->{
				for (ISocketMonitor monitor : monitors) {
					monitor.onSocketConnected(socket);
				}
			});
		}
	}
	void onSocketConnecting(PSocket socket) {
		if (monitors != null && !monitors.isEmpty()) {
			ThreadUtils.runAsync(()->{
				for (ISocketMonitor monitor : monitors) {
					monitor.onSocketConnecting(socket);
				}
			});
		}
	}
	void onSocketDisconnected(PSocket socket) {
		if (monitors != null && !monitors.isEmpty()) {
			ThreadUtils.runAsync(()->{
				for (ISocketMonitor monitor : monitors) {
					monitor.onSocketDisconnected(socket);
				}
			});
		}
	}
	@Override
	public void setStore(ISocketStore store) {
		this.store.setWrapped(store);
	}
}
