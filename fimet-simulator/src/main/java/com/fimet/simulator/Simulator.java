package com.fimet.simulator;

import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.LoggerFactory;import org.slf4j.Logger;

import com.fimet.IEventManager;
import com.fimet.IParserManager;
import com.fimet.ISimulatorModelManager;
import com.fimet.ISocketManager;
import com.fimet.Manager;
import com.fimet.event.SimulatorEvent;
import com.fimet.net.IConnectable;
import com.fimet.net.IConnectionListener;
import com.fimet.net.ISocket;
import com.fimet.net.ISocketListener;
import com.fimet.net.NullConnectionListener;
import com.fimet.parser.IMessage;
import com.fimet.parser.IParser;
import com.fimet.parser.Message;
import com.fimet.simulator.ISimulator;
import com.fimet.simulator.ISimulatorListener;
import com.fimet.simulator.ISimulatorModel;
import com.fimet.utils.Args;

/**
 * 
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 */
public class Simulator  implements ISimulator, ISocketListener, IConnectionListener, IConnectable {
	private static Logger logger = LoggerFactory.getLogger(Simulator.class);
	static IEventManager eventManager = Manager.getManager(IEventManager.class);
	static ISocketManager socketManager = Manager.getManager(ISocketManager.class);
	static IParserManager parserManager = Manager.getManager(IParserManager.class);
	static ISimulatorModelManager simulatorModelManager = Manager.getManager(ISimulatorModelManager.class);
	static final String APPROVAL_CODE = "00";
	volatile ISocket socket;
	private ISimulatorListener listener;
	private IConnectionListener connectionListener;
	private IParser parser;
	private ISimulatorModel model;
	private ISimulatorStore store;
	private String name;
	private AtomicLong numOfResponses;
	private AtomicLong numOfApprovals;
	private String group;
	private SimulatorThreadPool threadPool;
	public Simulator(IESimulator e, SimulatorThreadPool threadPool) {
		Args.notNull("Simulator Entity", e);
		Args.notNull("Simulator Model", e.getModel());
		Args.notNull("Simulator Parser", e.getParser());
		Args.notNull("Simulator Name", e.getName());
		Args.notNull("Simulator Thread Pool", threadPool);
		
		this.threadPool = threadPool;
		this.group = e.getGroup()!=null?e.getGroup():"None";
		this.name = e.getName();
		this.numOfResponses = new AtomicLong(0);
		this.numOfApprovals = new AtomicLong(0);
		this.socket = socketManager.getSocket(e, this);
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
		}
	}
	@Override
	public ISocket getSocket() {
		return socket;
	}
	@Override
	public synchronized boolean isConnected() {
		return socket.isConnected();
	}
	@Override
	public synchronized boolean isDisconnected() {
		return socket.isDisconnected();
	}
	@Override
	public synchronized boolean isConnecting() {
		return socket.isConnecting();
	}
	@Override
	public void onConnecting(IConnectable socket) {
		connectionListener.onConnecting(this);
		eventManager.fireEvent(SimulatorEvent.SIMULATOR_CONNECTING, this, this);
	}
	@Override
	public void onConnected(IConnectable socket) {
		connectionListener.onConnected(this);
		eventManager.fireEvent(SimulatorEvent.SIMULATOR_CONNECTED, this, this);
	}
	@Override
	public void onDisconnected(IConnectable socket) {
		connectionListener.onDisconnected(this);
		eventManager.fireEvent(SimulatorEvent.SIMULATOR_DISCONNECTED, this, this);
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
	public void writeMessage(IMessage msg) {
		threadPool.getNextSimulatorThread().simulateWrite(this, msg);
	}
	@Override
	public void onSocketRead(byte[] bytes) {
		threadPool.getNextSimulatorThread().simulateRead(this, bytes);
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
			logger.error("Error on write message "+this+": "+message, e);
		}
	}
	@Override
	public void doReadMessage(byte[] bytes) {
		try {
			IMessage message = parser.parseMessage(bytes);
			message.setProperty(Message.ADAPTER, socket.getAdapter());
			storeIncoming(message, bytes);
			listener.onSimulatorReadMessage(this, message, bytes);
			IMessage response = model.simulateResponse(message);
			if (response != null) {
				byte[] iso = listener.onSimulatorWriteMessage(this, response);
				storeOutgoing(response, iso);
				if (iso != null) {
					numOfResponses.getAndIncrement();
					socket.write(iso);
				}
			} else if (message.hasField("39")) {
				if (APPROVAL_CODE.equals(message.getValue("39")))
					numOfApprovals.getAndIncrement();
			} else if (logger.isDebugEnabled()) {
				logger.debug("Simulator "+name+" not configured for "+message.getProperty(IMessage.MTI));
			}
		} catch (Throwable e) {
			logger.error("Error on read message("+(bytes!= null?bytes.length:0)+") "+this+":\n"+new String(bytes)+"\n",e);
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
			logger.warn("Error on store outgoing message", e);
		}
	}
	private void storeIncoming(IMessage message, byte[] bytes) {
		try {
			store.storeIncoming(this, message, bytes);
		} catch (Throwable e) {
			logger.warn("Error on store incoming message", e);
		}
	}
	public long getNumOfApprovals() {
		return numOfApprovals.get();
	}
	@Override
	public String getName() {
		return name;
	}
	@Override
	public String getGroup() {
		return group;
	}
}
