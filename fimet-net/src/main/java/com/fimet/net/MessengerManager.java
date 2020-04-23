package com.fimet.net;

import java.util.HashMap;
import java.util.Map;

import com.fimet.core.IMessengerManager;
import com.fimet.core.Manager;
import com.fimet.core.net.IMessenger;
import com.fimet.core.net.IMessengerThread;
import com.fimet.core.net.ISocket;

/**
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 *
 */
	
public class MessengerManager implements IMessengerManager {
	static final int NUMBER_OF_THREADS = 5;	
	private Map<Integer, IMessenger> connections = new HashMap<>();
	private MessengerThread next;
	private MessengerThread head;
	public MessengerManager() {
		Integer numberOfThreads = Manager.getPropertyInteger("messenger.numberOfThreads");
		if (numberOfThreads == null) {
			numberOfThreads = NUMBER_OF_THREADS;
		}
		this.head = new MessengerThread(0);
		if (numberOfThreads > 1) {
			MessengerThread prev = head;
			for (int i = 1; i < numberOfThreads; i++) {
				next = new MessengerThread(i);	
				prev.next = next;
				prev = next;
			}
			next.next = head;
		} else {
			head.next = head;
		}
		next = head;
	}
	@Override
	public boolean isConnected(ISocket socket) {
		return connections.containsKey(hashCode(socket)) && connections.get(hashCode(socket)).isConnected();
	}
	@Override
	public boolean isDisconnected(ISocket socket) {
		return !connections.containsKey(hashCode(socket)) || connections.get(hashCode(socket)).isDisconnected();
	}
	@Override
	public boolean isConnecting(ISocket socket) {
		return connections.containsKey(hashCode(socket)) && connections.get(hashCode(socket)).isConnecting();
	}
	@Override
	public IMessenger getMessenger(ISocket socket) {
		if (!connections.containsKey(hashCode(socket))) {
			connections.put(hashCode(socket), new Messenger(socket));	
		}
		return connections.get(hashCode(socket));
	}
	@Override
	public IMessenger connect(ISocket socket) {
		IMessenger messenger = getMessenger(socket);
		messenger.connect();
		return messenger;
	}
	@Override
	public void disconnect(ISocket socket) {
		if (connections.containsKey(hashCode(socket))) {
			connections.get(hashCode(socket)).disconnect();
			connections.remove(hashCode(socket));
		}
	}
	@Override
	public void disconnectAll() {
		for (Map.Entry<Integer, IMessenger> e : connections.entrySet()) {
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
	public int hashCode(ISocket socket) {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((socket == null || socket.getAddress() == null) ? 0 : socket.getAddress().hashCode());
		result = prime * result + (socket == null || socket.isServer() ? 1231 : 1237);
		result = prime * result + ((socket == null || socket.getPort() == null) ? 0 : socket.getPort().hashCode());
		return result;
	}
	@Override
	synchronized public IMessengerThread getNextMessengerThread() {
		return next = next.next;
	}

}
