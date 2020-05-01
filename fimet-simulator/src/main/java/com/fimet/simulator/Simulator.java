package com.fimet.simulator;

import com.fimet.commons.FimetLogger;
import com.fimet.IParserManager;
import com.fimet.IUseCaseManager;
import com.fimet.ISimulatorManager;
import com.fimet.ISimulatorModelManager;
import com.fimet.ISocketManager;
import com.fimet.Manager;
import com.fimet.iso8583.parser.IParser;
import com.fimet.iso8583.parser.Message;
import com.fimet.net.ISocket;
import com.fimet.net.ISocketListener;
import com.fimet.net.IConnectable;
import com.fimet.net.IConnectionListener;
import com.fimet.net.NullConnectionListener;
import com.fimet.net.NullSimulatorListener;
import com.fimet.simulator.ISimulator;
import com.fimet.simulator.ISimulatorListener;
import com.fimet.simulator.ISimulatorModel;
import com.fimet.simulator.PSimulator;

/**
 * 
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 */
public class Simulator  implements ISimulator, ISocketListener, IConnectionListener, IConnectable {
	static ISocketManager socketManager = Manager.get(ISocketManager.class);
	static ISimulatorManager simulatorManager = Manager.get(ISimulatorManager.class);
	static IUseCaseManager executorManager = Manager.get(IUseCaseManager.class);
	static IParserManager parserManager = Manager.get(IParserManager.class);
	static ISimulatorModelManager simulatorModelManager = Manager.get(ISimulatorModelManager.class);
	
	ISocket socket;
	ISimulatorListener listener;
	IConnectionListener connectionListener;
	IParser parser;
	ISimulatorModel model;
	Integer idSimulator;
	public Simulator(PSimulator pSimulator) {
		if (pSimulator == null)
			throw new NullPointerException("Simulator parameters are null");
		if (pSimulator.getModel() == null) {
			throw new NullPointerException("Simulator model is null for "+pSimulator);
		}
		if (pSimulator.getParser() == null) {
			throw new NullPointerException("Simulator initialization parser is null for "+pSimulator);
		}
		this.socket = socketManager.getSocket(pSimulator.getPSocket(), this);
		this.listener = NullSimulatorListener.INSTANCE;
		this.connectionListener = NullConnectionListener.INSTANCE;
		this.parser = parserManager.getParser(pSimulator.getParser());
		this.idSimulator = pSimulator.getIdSimulator();
		this.model = simulatorModelManager.getSimulatorModel(idSimulator);
	}
	/**
	 * Attempt to connect the iSocket
	 */
	public void connect() {
		if (isDisconnected()) {// Maybe socket is in CONNECTED or CONNECTING status
			socket.connect();
		}
	}
	public void disconnect() {
		if (socket != null) {
			socket.disconnect();
			socket = null;
		}
	}
	public ISocket getSocket() {
		return socket;
	}
	public synchronized boolean isConnected() {
		return socket != null && socket.isConnected();
	}
	public synchronized boolean isDisconnected() {
		return socket == null || socket.isDisconnected();
	}
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
	public void setConnectionListener(IConnectionListener listener) {
		this.connectionListener = listener != null ? listener : NullConnectionListener.INSTANCE;
	}
	@Override
	public String toString() {
		return model+" "+socket;
	}
	@Override
	public IParser getParser() {
		return parser;
	}
	@Override
	public void free() {
		model.free();
	}
	@Override
	public int hashCode() {
		return socket.hashCode();
	}
	@Override
	public Message simulateRequest(Message message) {
		return model.simulateRequest(message);
	}
	@Override
	public Message simulateResponse(Message message) {
		return model.simulateResponse(message);
	}
	public void writeMessage(Message msg) {
		simulatorManager.getNextSimulatorThread().simulateWrite(this, msg);
	}
	public void onSocketRead(byte[] bytes) {
		simulatorManager.getNextSimulatorThread().simulateRead(this, bytes);
	}
	@Override
	public void doWriteMessage(Message msg) {
		try {
			msg = model.simulateRequest(msg);
			byte[] iso = listener.onSimulatorWriteMessage(this, msg);
			if (iso != null) {
				socket.write(iso);
			}
		} catch (Throwable e) {
			FimetLogger.error(SimulatorThread.class,"Error on write message "+this+": "+msg, e);
		}
	}
	@Override
	public void doReadMessage(byte[] bytes) {
		try {
			Message message = (Message)parser.parseMessage(bytes);
			listener.onSimulatorReadMessage(this, message, bytes);
			Message response = model.simulateResponse(message);
			if (response != null) {
				byte[] iso = listener.onSimulatorWriteMessage(this, response);
				if (iso != null) {
					socket.write(iso);
				}
			}
		} catch (Throwable e) { 
			FimetLogger.error(Manager.class, "Error on read message("+(bytes!= null?bytes.length:0)+") "+this+":\n"+new String(bytes)+"\n",e);
		}			
	}
	@Override
	public ISimulatorModel getModel() {
		return model;
	}
}
