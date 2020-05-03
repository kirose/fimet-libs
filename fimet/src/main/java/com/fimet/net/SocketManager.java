package com.fimet.net;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fimet.ISocketManager;
import com.fimet.commons.utils.ThreadUtils;

public class SocketManager implements ISocketManager {
	private Map<Integer, ISocket> connections = new ConcurrentHashMap<>();
	private List<ISocketMonitor> monitors = new ArrayList<>();
	public SocketManager() {
	}
	@Override
	public ISocket getSocket(PSocket socket, ISocketListener listener) {
		int hash = socket.hashCode();
		if (!connections.containsKey(hash)) {
			if (socket.isServer()) {
				connections.put(hash, new AdaptedSocketServer(socket, listener));
			} else {
				connections.put(hash, new AdaptedSocketClient(socket, listener));
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
	@Override
	public void free() {}
	@Override
	public void saveState() {}
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
}
