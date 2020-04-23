package com.fimet.core.net;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fimet.commons.FimetLogger;
import com.fimet.core.IMessengerManager;
import com.fimet.core.Manager;
import com.fimet.core.iso8583.parser.Message;

public class Connector implements IMessengerListener {
	static IMessengerManager messengerManager = Manager.get(IMessengerManager.class);
	private IConnectorOnConnect onConnectListener = NullConnectorOnConnect.INSTANCE;
	private IConnectorOnConnectAll onConnectAllListener = NullConnectorOnConnectAll.INSTANCE;
	private IMessengerListener messengerListener = NullMessengerListener.INSTANCE;
	private IConnectable connectable;
	private Set<ISocket> toConnect = new HashSet<ISocket>();
	private boolean async;
	public Connector(IConnectable connectable, IConnectorOnConnectAll listener) {
		this.connectable = connectable;
		this.toConnect.addAll(connectable.getConnections());
		this.setOnConnectAllListener(listener);
		if (listener instanceof IConnectorOnConnect) {
			this.setOnConnectListener((IConnectorOnConnect)listener);
		}
	}
	public Connector(IConnectable connectable) {
		this.connectable = connectable;
		this.toConnect.addAll(connectable.getConnections());
	}
	public Connector(ISocket ... sockets) {
		this.connectable = new DefaultConnector(sockets);
		this.toConnect.addAll(connectable.getConnections());
	}
	public Connector(List<ISocket> connections) {
		this.async = false;
		this.connectable = new DefaultConnector(connections);
		this.toConnect.addAll(connectable.getConnections());
	}
	synchronized private void onConnected(IMessenger messenger) {
		messenger.setListener(messengerListener);
		toConnect.remove(messenger.getConnection());
		onConnectListener.onConnectorConnect(messenger);
		if(toConnect.isEmpty()) {
			onConnectAllListener.onConnectorConnectAll(connectable);
			if (!async) {
				synchronized (this) {
					this.notify();
				}
			}
		}
	}
	private void _connectSockets() {
		for (ISocket iSocket : connectable.getConnections()) {
			IMessenger messenger = messengerManager.getMessenger(iSocket);
			messenger.setListener(this);
			if (messenger.isDisconnected()) {
				messenger.connect();
			} else if (messenger.isConnected()) {
				onConnected(messenger);
			}
		}
	}
	public void connectAsync() {
		async = true;
		_connectSockets();
	}
	public void connectSync() {
		async = true;
		_connectSockets();
		if(!toConnect.isEmpty()) {
			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					FimetLogger.error(Connector.class, "Connector Error",e);
				}
			}
		}
	}
	public Connector setAsync(boolean async) {
		this.async = async;
		return this;
	}
	public Connector setOnConnectAllListener(IConnectorOnConnectAll onConnectAllListener) {
		this.onConnectAllListener = onConnectAllListener != null ? onConnectAllListener : NullConnectorOnConnectAll.INSTANCE;
		return this;
	}
	public Connector setOnConnectListener(IConnectorOnConnect onConnectListener) {
		this.onConnectListener = onConnectListener != null ? onConnectListener : NullConnectorOnConnect.INSTANCE;
		return this;
	}
	public static interface IConnectable {
		List<ISocket> getConnections();
	}
	public static interface IConnectorOnConnect {
		void onConnectorConnect(IMessenger messenger);
	}
	public static interface IConnectorOnConnectAll {
		void onConnectorConnectAll(IConnectable connectable);
	}
	@Override
	public void onMessengerConnected(IMessenger messenger) {
		messenger.setListener(null);
		onConnected(messenger);
	}
	@Override
	public void onMessengerConnecting(IMessenger messenger) {}
	@Override
	public void onMessengerDisconnected(IMessenger messenger) {}
	@Override
	public void onMessengerWriteToSocket(IMessenger messenger, byte[] message) {}
	@Override
	public void onMessengerWriteMessage(IMessenger messenger, Message message) {}
	@Override
	public void onMessengerReadFromSocket(IMessenger messenger, byte[] message) {}
	@Override
	public void onMessengerReadMessage(IMessenger messenger, Message message) {}
	public class DefaultConnector implements IConnectable {
		List<ISocket> connections;
		public DefaultConnector(ISocket[] sockets) {
			connections = new ArrayList<>(sockets.length);
			for (ISocket s : sockets) {
				connections.add(s);
			}
		}
		public DefaultConnector(List<ISocket> connections) {
			this.connections = connections;
		}
		@Override
		public List<ISocket> getConnections() {
			return connections;
		}
	}
}
