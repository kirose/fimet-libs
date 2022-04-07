package com.fimet;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fimet.IEventManager;
import com.fimet.ISocketManager;
import com.fimet.dao.ISocketDAO;
import com.fimet.event.Event;
import com.fimet.event.NetEvent;
import com.fimet.net.AdaptedSocket;
import com.fimet.net.AdaptedSocketClient;
import com.fimet.net.AdaptedSocketServer;
import com.fimet.net.IESocket;
import com.fimet.net.ISocket;
import com.fimet.net.ISocketListener;
import com.fimet.net.ISocketStore;
import com.fimet.net.NullSocketStore;
import com.fimet.net.SocketStoreWrapper;
import com.fimet.utils.ArrayUtils;

@Component
public class SocketManager implements ISocketManager {
	@Autowired private IEventManager eventManager;
	private Map<String, ISocket> connections;
	private SocketStoreWrapper store;
	@Autowired private ISocketDAO<? extends IESocket> dao;
	public SocketManager() {
	}
	@Override
	public ISocket getSocket(IESocket socket, ISocketListener listener) {
		if (!connections.containsKey(socket.getName())) {
			if (socket.isServer()) {
				AdaptedSocketServer socketServer = new AdaptedSocketServer(socket, listener);
				socketServer.setStore(store);
				connections.put(socket.getName(), socketServer);
			} else {
				AdaptedSocketClient socketClient = new AdaptedSocketClient(socket, listener);
				socketClient.setStore(store);
				connections.put(socket.getName(), socketClient);
			} 
		}
		return connections.get(socket.getName());
	}
	@Override
	public ISocket getSocket(IESocket socket) {
		return getSocket(socket, null);
	}
	@Override
	public ISocket connect(IESocket socket, ISocketListener listener) {
		ISocket messenger = getSocket(socket, listener);
		messenger.connect();
		return messenger;
	}
	@Override
	public void disconnect(String name) {
		if (connections.containsKey(name)) {
			connections.get(name).disconnect();
		}
	}
	@Override
	public void disconnectAll() {
		for (Map.Entry<String, ISocket> e : connections.entrySet()) {
			e.getValue().disconnect();
		}
		connections.clear();
	}
	public void setSocketTimeReconnect(int sec) {
		AdaptedSocket.RECONNECTION_TIME = sec*1000;
	}
	@Override
	public void setStore(ISocketStore store) {
		this.store.setWrapped(store);
	}
	@Override
	public ISocket remove(String name) {
		if (connections.containsKey(name)) {
			ISocket socket = connections.remove(name);
			if (!socket.isConnected()) {
				socket.disconnect();
			}
			eventManager.fireEvent(Event.SOCKET_REMOVED, this, socket);
			return socket;
		}
		return null;
	}
	@Override
	public ISocket reload(String name) {
		if (connections.containsKey(name)) {
			connections.get(name).disconnect();
			connections.get(name).connect();
		}
		return null;
	}
	@Override
	public List<ISocket> getSockets() {
		return ArrayUtils.copyValuesAsList(this.connections);
	}
	@PostConstruct
	@Override
	public void start() {
		store = new SocketStoreWrapper(NullSocketStore.INSTANCE);
		reload(true);
	}
	@Override
	public void reload() {
		reload(true);
	}
	private void reload(boolean fireEvent) {
		if (connections!=null&&!connections.isEmpty()) {
			for (Entry<String, ISocket> e : connections.entrySet()) {
				e.getValue().disconnect();
			}
		}
		connections = new ConcurrentHashMap<>();
		List<? extends IESocket> all = dao.findAll();
		if (all!=null&&!all.isEmpty()) {
			for (IESocket e : all) {
				getSocket(e);
			}
		}
		if (fireEvent) {
			eventManager.fireEvent(NetEvent.SOCKET_MANAGER_RELOADED, this, getSockets());
		}
	}
	@Override
	public ISocket getSocket(String name) {
		return connections.get(name);
	}
	@Override
	public ISocket getSocket(String name, ISocketListener listener) {
		if (connections.containsKey(name)) {
			ISocket socket = connections.get(name);
			socket.setListener(listener);
			return socket;
		} else {
			return null;
		}
	}
	@Override
	public void stop() {
		for (Map.Entry<String, ISocket> e : connections.entrySet()) {
			e.getValue().disconnect();
		}
	}
}
