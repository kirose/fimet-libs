package com.fimet.net;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fimet.ISimulatorManager;
import com.fimet.ITimerManager;
import com.fimet.Manager;
import com.fimet.commons.FimetLogger;
import com.fimet.timer.ITimerListener;
import com.fimet.timer.Scheduled;

public class MultiConnector implements IConnectionListener, ITimerListener {
	private static final long MAX_WAITING_TIME = Manager.getPropertyLong("multiConnector.maxWaitingTime", 3000L);
	static ISimulatorManager simulatorManager = Manager.get(ISimulatorManager.class);
	static ITimerManager timerManager = Manager.get(ITimerManager.class);
	private IMultiConnectorListener listener = NullMultiConnectorListener.INSTANCE;
	private IConnectionListener connectionListener = NullConnectionListener.INSTANCE;
	private IMultiConnectable multiConnectable;
	private Set<Integer> toConnect = new HashSet<Integer>();
	private boolean async;
	private Scheduled scheduled;
	public MultiConnector(IMultiConnectable multiConnectable, IMultiConnectorListener listener) {
		this.multiConnectable = multiConnectable;
		for (IConnectable p : multiConnectable.getConnectables()) {
			this.toConnect.add(p.hashCode());
		}
		this.setMultiConnectiorListener(listener);
		if (listener instanceof IConnectionListener) {
			connectionListener = (IConnectionListener)listener;
		}
	}
	public MultiConnector(IMultiConnectable connectable) {
		this(connectable, (connectable instanceof IMultiConnectorListener) ? (IMultiConnectorListener)connectable : null);
	}
	public MultiConnector(IConnectable ... connectables) {
		this.multiConnectable = new DefaultMultiConnectable(connectables);
		for (IConnectable p : connectables) {
			this.toConnect.add(p.hashCode());
		}
	}
	public MultiConnector(List<IConnectable> connectables) {
		this.multiConnectable = new DefaultMultiConnectable(connectables);
		for (IConnectable p : connectables) {
			this.toConnect.add(p.hashCode());
		}
	}
	@Override
	synchronized public void onConnected(IConnectable connectable) {
		connectable.setConnectionListener(connectionListener);
		toConnect.remove(connectable.hashCode());
		listener.onConnectorConnect(connectable);
		if(toConnect.isEmpty()) {
			scheduled.cancel();
			listener.onConnectorConnectAll(multiConnectable);
			if (!async) {
				synchronized (this) {
					this.notify();
				}
			}
		}
	}
	private void _connectSimulators() {
		scheduled = timerManager.schedule(this, MAX_WAITING_TIME, this);
		for (IConnectable connectable : multiConnectable.getConnectables()) {
			if (connectable.isDisconnected()) {
				connectable.setConnectionListener(this);
				connectable.connect();
			} else if (connectable.isConnected()) {
				onConnected(connectable);
			}
		}
	}
	public void connectAsync() {
		async = true;
		_connectSimulators();
	}
	public void connectSync() {
		async = true;
		_connectSimulators();
		if(!toConnect.isEmpty()) {
			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					FimetLogger.error(MultiConnector.class, "Connector Error",e);
				}
			}
		}
	}
	public MultiConnector setAsync(boolean async) {
		this.async = async;
		return this;
	}
	public MultiConnector setMultiConnectiorListener(IMultiConnectorListener listener) {
		this.listener = listener != null ? listener : NullMultiConnectorListener.INSTANCE;
		return this;
	}
	public static interface IMultiConnectable {
		List<IConnectable> getConnectables();
	}
	public static interface IMultiConnectorListener {
		void onConnectorConnect(IConnectable simulator);
		void onConnectorConnectAll(IMultiConnectable connectable);
		void onConnectorTimeout(IMultiConnectable connectable);
	}
	@Override
	public void onConnecting(IConnectable simulator) {}
	@Override
	public void onDisconnected(IConnectable simulator) {}
	public class DefaultMultiConnectable implements IMultiConnectable {
		List<IConnectable> connections;
		public DefaultMultiConnectable(IConnectable[] sockets) {
			connections = new ArrayList<>(sockets.length);
			for (IConnectable s : sockets) {
				connections.add(s);
			}
		}
		public DefaultMultiConnectable(List<IConnectable> connections) {
			this.connections = connections;
		}
		@Override
		public List<IConnectable> getConnectables() {
			return connections;
		}
	}
	@Override
	public void onTimeout(Object o) {
		List<IConnectable> connectables = multiConnectable.getConnectables();
		for (IConnectable iConnectable : connectables) {
			iConnectable.setConnectionListener(connectionListener);
		}
		listener.onConnectorTimeout(multiConnectable);
	}
}
