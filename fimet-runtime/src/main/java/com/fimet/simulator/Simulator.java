package com.fimet.simulator;

import java.util.concurrent.atomic.AtomicLong;

import com.fimet.FimetLogger;
import com.fimet.IParserManager;
import com.fimet.ISimulatorManager;
import com.fimet.ISimulatorModelManager;
import com.fimet.ISocketManager;
import com.fimet.Manager;
import com.fimet.parser.IMessage;
import com.fimet.parser.IParser;
import com.fimet.parser.Message;
import com.fimet.simulator.ISimulator;
import com.fimet.simulator.ISimulatorListener;
import com.fimet.simulator.ISimulatorModel;
import com.fimet.socket.IConnectable;
import com.fimet.socket.IConnectionListener;
import com.fimet.socket.ISocket;
import com.fimet.socket.ISocketListener;
import com.fimet.socket.NullConnectionListener;
import com.fimet.socket.NullSimulatorListener;
import com.fimet.socket.PSocket;

/**
 * 
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 */
public class Simulator  implements ISimulator, ISocketListener, IConnectionListener, IConnectable {
	static ISocketManager socketManager = Manager.get(ISocketManager.class);
	static ISimulatorManager simulatorManager = Manager.get(ISimulatorManager.class);
	static IParserManager parserManager = Manager.get(IParserManager.class);
	static ISimulatorModelManager simulatorModelManager = Manager.get(ISimulatorModelManager.class);
	static final String APPROVAL = "00";
	ISocket socket;
	ISimulatorListener listener;
	IConnectionListener connectionListener;
	IParser parser;
	ISimulatorModel model;
	ISimulatorStore store;
	String name;
	AtomicLong numOfApprovals;
	public Simulator(IESimulator e) {
		if (e == null)
			throw new NullPointerException("Simulator parameters are null");
		if (e.getModel() == null) {
			throw new NullPointerException("Simulator model is null for "+e);
		}
		if (e.getParser() == null) {
			throw new NullPointerException("Simulator initialization parser is null for "+e);
		}
		this.name = e.getName();
		this.numOfApprovals = new AtomicLong(0);
		PSocket pSocket = new PSocket(e.getAddress(), e.getPort(), e.isServer(), e.getAdapter());
		this.socket = socketManager.getSocket(pSocket, this);
		this.listener = NullSimulatorListener.INSTANCE;
		this.connectionListener = NullConnectionListener.INSTANCE;
		this.parser = parserManager.getParser(e.getParser());
		this.model = simulatorModelManager.getSimulatorModel(e.getModel());
		this.store = NullSimulatorStore.INSTANCE;
	}
	/**
	 * Attempt to connect the iSocket
	 */
	@Override
	public void connect() {
		if (isDisconnected()) {// Maybe socket is in CONNECTED or CONNECTING status
			socket.connect();
		}
	}
	@Override
	public void disconnect() {
		if (socket != null) {
			socket.disconnect();
			socket = null;
		}
	}
	@Override
	public ISocket getSocket() {
		return socket;
	}
	@Override
	public synchronized boolean isConnected() {
		return socket != null && socket.isConnected();
	}
	@Override
	public synchronized boolean isDisconnected() {
		return socket == null || socket.isDisconnected();
	}
	@Override
	public synchronized boolean isConnecting() {
		return socket != null && socket.isConnecting();
	}
	@Override
	public void onConnecting(IConnectable simulator) {
		connectionListener.onConnecting(this);
	}
	@Override
	public void onConnected(IConnectable simulator) {
		connectionListener.onConnected(this);
	}
	@Override
	public void onDisconnected(IConnectable simulator) {
		connectionListener.onDisconnected(this);
	}
	@Override
	public void setListener(ISimulatorListener listener) {
		this.listener = listener != null ? listener : NullSimulatorListener.INSTANCE;
	}
	@Override
	public IConnectionListener getConnectionListener() {
		return connectionListener;
	}
	@Override
	public void setConnectionListener(IConnectionListener listener) {
		this.connectionListener = listener != null ? listener : NullConnectionListener.INSTANCE;
	}
	@Override
	public String toString() {
		return name+" "+model+" "+socket;
	}
	@Override
	public IParser getParser() {
		return parser;
	}
	@Override
	public int hashCode() {
		return socket.hashCode();
	}
	@Override
	public IMessage simulateRequest(IMessage message) {
		return model.simulateRequest(message);
	}
	@Override
	public IMessage simulateResponse(IMessage message) {
		return model.simulateResponse(message);
	}
	@Override
	public void writeMessage(IMessage msg) {
		simulatorManager.getNextSimulatorThread().simulateWrite(this, msg);
	}
	@Override
	public void onSocketRead(byte[] bytes) {
		simulatorManager.getNextSimulatorThread().simulateRead(this, bytes);
	}
	@Override
	public void doWriteMessage(IMessage message) {
		try {
			message.setProperty(Message.ADAPTER, socket.getAdapter());
			message = model.simulateRequest(message);
			byte[] iso = listener.onSimulatorWriteMessage(this, message);
			if (iso != null) {
				storeOutgoing(message, iso);
				socket.write(iso);
			}
		} catch (Throwable e) {
			FimetLogger.error(SimulatorThread.class,"Error on write message "+this+": "+message, e);
		}
	}
	@Override
	public void doReadMessage(byte[] bytes) {
		try {
			IMessage message = (IMessage)parser.parseMessage(bytes);
			message.setProperty(Message.ADAPTER, socket.getAdapter());
			storeIncoming(message, bytes);
			listener.onSimulatorReadMessage(this, message, bytes);
			IMessage response = model.simulateResponse(message);
			if (response != null) {
				byte[] iso = listener.onSimulatorWriteMessage(this, response);
				storeOutgoing(response, iso);
				if (iso != null) {
					socket.write(iso);
				}
			} else if (APPROVAL.equals(message.getValue("39"))) {
				this.numOfApprovals.getAndIncrement();
			} else if (FimetLogger.isEnabledDebug()) {
				FimetLogger.debug(getClass(), "Simulator not configured for "+message.getProperty(IMessage.MTI));
			}
		} catch (Throwable e) {
			FimetLogger.error(Manager.class, "Error on read message("+(bytes!= null?bytes.length:0)+") "+this+":\n"+new String(bytes)+"\n",e);
		}
	}
	@Override
	public ISimulatorModel getModel() {
		return model;
	}
	public void setStore(ISimulatorStore store) {
		this.store = store != null ? store : NullSimulatorStore.INSTANCE; 
	}
	private void storeOutgoing(IMessage message, byte[] bytes) {
		try {
			store.storeOutgoing(this, message, bytes);
		} catch (Throwable e) {
			FimetLogger.warning(Simulator.class, "Error on store outgoing message", e);
		}
	}
	private void storeIncoming(IMessage message, byte[] bytes) {
		try {
			store.storeIncoming(this, message, bytes);
		} catch (Throwable e) {
			FimetLogger.warning(Simulator.class, "Error on store incoming message", e);
		}
	}
	public long getNumOfApprovals() {
		return numOfApprovals.get();
	}
	@Override
	public String getName() {
		return name;
	}
}
