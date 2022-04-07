package com.fimet.net;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.LoggerFactory;import org.slf4j.Logger;

import com.fimet.ISimulatorManager;
import com.fimet.ITimerManager;
import com.fimet.Manager;
import com.fimet.net.IConnectable;
import com.fimet.net.IConnectionListener;
import com.fimet.net.IMultiConnectable;
import com.fimet.simulator.ISimulator;
import com.fimet.utils.ITimerListener;
import com.fimet.utils.Scheduled;

public class MultiConnector implements IConnectionListener, ITimerListener {
	private static Logger logger = LoggerFactory.getLogger(MultiConnector.class);
	private static final long MAX_WAITING_TIME = Manager.getPropertyLong("multiConnector.maxWaitingTime", 3000L);
	static ISimulatorManager simulatorManager = Manager.getManager(ISimulatorManager.class);
	static ITimerManager timerManager = Manager.getManager(ITimerManager.class);
	private IMultiConnectorListener listener;
	private IMultiConnectable multiConnectable;
	private Map<IConnectable, IConnectionListener> toConnect;
	private boolean async;
	private Scheduled scheduled;
	public MultiConnector(IMultiConnectable multiConnectable) {
		_init(multiConnectable);
	}
	public MultiConnector(IConnectable ... connectables) {
		_init(new DefaultMultiConnectable(connectables));
	}
	public MultiConnector(List<IConnectable> connectables) {
		_init(new DefaultMultiConnectable(connectables));
	}
	private void _init(IMultiConnectable multiConnectable) {
		this.multiConnectable = multiConnectable;
		this.listener = NullMultiConnectorListener.INSTANCE;
		this.toConnect = new ConcurrentHashMap<IConnectable, IConnectionListener>();
		prepareToConnect(multiConnectable.getConnectables());
	}
	@Override
	synchronized public void onConnected(IConnectable connectable) {
		IConnectionListener original = toConnect.remove(connectable);
		connectable.setConnectionListener(original);
		original.onConnected(connectable);
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
	private void doConnect() {
		if (toConnect.isEmpty()) {
			listener.onConnectorConnectAll(multiConnectable);
		} else {
			scheduled = timerManager.schedule(this, MAX_WAITING_TIME, this);
			for (IConnectable connectable : multiConnectable.getConnectables()) {
				if (connectable.isDisconnected()) {
					connectable.connect();
				} else if (connectable.isConnected()) {
					onConnected(connectable);
				}
			}
		}
	}
	public void connectAsync(IMultiConnectorListener listener) {
		async = true;
		this.listener = listener != null ? listener : NullMultiConnectorListener.INSTANCE;
		doConnect();
	}
	public void connectSync() {
		async = true;
		doConnect();
		if(!toConnect.isEmpty()) {
			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					logger.error("Connector Error",e);
				}
			}
		}
	}
	private void prepareToConnect(List<IConnectable> connectables) {
		for (IConnectable c : connectables) {
			if (c.isDisconnected()) {
				if (c instanceof ISimulator) {
					c =((ISimulator)c).getSocket();
				}
				if (!toConnect.containsKey(c)) {
					toConnect.put(c, c.getConnectionListener());
					c.setConnectionListener(this);
				}
			}
		}
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
		for (Entry<IConnectable, IConnectionListener> e : toConnect.entrySet()) {
			e.getKey().setConnectionListener(e.getValue());
		}
		listener.onConnectorTimeout(multiConnectable);
	}
}
