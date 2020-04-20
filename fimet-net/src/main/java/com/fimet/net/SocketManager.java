package com.fimet.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fimet.commons.utils.ThreadUtils;
import com.fimet.core.ISocketManager;
import com.fimet.core.net.IAdaptedSocket;
import com.fimet.core.net.IAdaptedSocketListener;
import com.fimet.core.net.ISocket;
import com.fimet.core.net.ISocketMonitor;

public class SocketManager implements ISocketManager {
	private Map<Integer, IAdaptedSocket> connections = new HashMap<>();
	private List<ISocketMonitor> monitors = new ArrayList<>();
	public SocketManager() {
	}
	@Override
	public boolean isConnected(ISocket socket) {
		int hash = hashCode(socket);
		return connections.containsKey(hash) && connections.get(hash).isConnected();
	}
	@Override
	public boolean isDisconnected(ISocket socket) {
		int hash = hashCode(socket);
		return !connections.containsKey(hash) || connections.get(hash).isDisconnected();
	}
	@Override
	public boolean isConnecting(ISocket socket) {
		int hash = hashCode(socket);
		return connections.containsKey(hash) && connections.get(hash).isConnecting();
	}
	@Override
	public IAdaptedSocket getSocket(ISocket socket, IAdaptedSocketListener listener) {
		int hash = hashCode(socket);
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
	public IAdaptedSocket connect(ISocket socket, IAdaptedSocketListener listener) {
		IAdaptedSocket messenger = getSocket(socket, listener);
		messenger.connect();
		return messenger;
	}
	@Override
	public void disconnect(ISocket socket) {
		int hash = hashCode(socket);
		if (connections.containsKey(hash)) {
			connections.get(hash).disconnect();
			connections.remove(hash);
		}
	}
	@Override
	public void disconnectAll() {
		for (Map.Entry<Integer, IAdaptedSocket> e : connections.entrySet()) {
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
	void onSocketConnected(ISocket socket) {
		if (monitors != null && !monitors.isEmpty()) {
			ThreadUtils.runAsync(()->{
				for (ISocketMonitor monitor : monitors) {
					monitor.onSocketConnected(socket);
				}
			});
		}
	}
	void onSocketConnecting(ISocket socket) {
		if (monitors != null && !monitors.isEmpty()) {
			ThreadUtils.runAsync(()->{
				for (ISocketMonitor monitor : monitors) {
					monitor.onSocketConnecting(socket);
				}
			});
		}
	}
	void onSocketDisconnected(ISocket socket) {
		if (monitors != null && !monitors.isEmpty()) {
			ThreadUtils.runAsync(()->{
				for (ISocketMonitor monitor : monitors) {
					monitor.onSocketDisconnected(socket);
				}
			});
		}
	}
	private int hashCode(ISocket socket) {
		final int prime = 31;
		int result = 1;
		result = prime * result + (socket == null ? 0 : ((socket.getAddress() == null) ? 0 : socket.getAddress().hashCode()));
		result = prime * result + (socket == null ? 0 : (socket.isServer() ? 1231 : 1237));
		result = prime * result + (socket == null ? 0 : ((socket.getPort() == null) ? 0 : socket.getPort().hashCode()));
		return result;
	}
}
