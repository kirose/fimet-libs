package com.fimet.net;

import com.fimet.commons.FimetLogger;
import com.fimet.core.ISocketManager;
import com.fimet.core.Manager;
import com.fimet.core.iso8583.parser.Message;
import com.fimet.core.net.IMessenger;
import com.fimet.core.net.IMessengerListener;
import com.fimet.core.net.ISocket;
import com.fimet.core.simulator.ISimulator;
import com.fimet.core.net.IAdaptedSocket;
import com.fimet.core.net.IAdaptedSocketListener;

/**
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 * Messenger
 */
	
public class Messenger implements IMessenger, IAdaptedSocketListener {
	static IMessengerListener NULL_LISTENER = new NullMessengerListener();
	static ISocketManager socketManager = Manager.get(ISocketManager.class);
	IAdaptedSocket socket;
	IMessengerListener listener;
	ISocket iSocket;
	int status;
	ISimulator simulator;

	public Messenger(ISocket iSocket) {
		super();
		if (iSocket == null)
			throw new NullPointerException("Socket model is null");
		if (iSocket.getSimulator() == null) {
			throw new NullPointerException("Simulator is null for "+socket);
		}
		this.iSocket = iSocket;
		this.simulator = iSocket.getSimulator();
		this.listener = NULL_LISTENER;
	}
	public void writeMessage(Message msg) {
		try {
			msg = simulator.simulateRequest(msg);
			listener.onMessengerWriteMessage(this, msg);
			byte[] iso = iSocket.getParser().formatMessage(msg);
			listener.onMessengerWriteToSocket(this, iso);
			socket.write(iso);
		} catch (Exception e) {
			FimetLogger.error(Messenger.class,"Error On Write Message: "+e.getMessage(), e);
		}
	}
	public void onSocketRead(byte[] bytes) {
		try {
			listener.onMessengerReadFromSocket(this, bytes);
			Message message = (Message)iSocket.getParser().parseMessage(bytes);
			message.setAdapter(iSocket.getAdapter());
			listener.onMessengerReadMessage(this, message);
			Message response = simulator.simulateResponse(message);
			if (response != null) {
				listener.onMessengerWriteMessage(this, response);
				byte[] iso = response.getParser().formatMessage(response);
				listener.onMessengerWriteToSocket(this, iso);
				socket.write(iso);
			}
		} catch (Exception e) { 
			FimetLogger.warning(Manager.class, "Error On Socket Read incoming message",e);
		}
	}
	/**
	 * Attempt to connect the iSocket
	 */
	public void connect() {
		if (isDisconnected()) {// Maybe socket is in CONNECTED or CONNECTING status
			socket = socketManager.getSocket(iSocket, this);
			socket.connect();
		}
	}
	public void disconnect() {
		if (socket != null) {
			socket.disconnect();
			socket = null;
		}
	}
	public IAdaptedSocket getSocket() {
		return socket;
	}
	public void setSocket(AdaptedSocketClient socket) {
		this.socket = socket;
	}
	@Override
	public ISocket getConnection() {
		return iSocket;
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
	public void onSocketConnecting() {
		listener.onMessengerConnecting(this);
	}
	@Override
	public void onSocketConnected() {
		listener.onMessengerConnected(this);
	}
	@Override
	public void onSocketDisconnected() {
		listener.onMessengerDisconnected(this);
	}
	public void setListener(IMessengerListener listener) {
		this.listener = listener != null ? listener : NULL_LISTENER;
	}
	public String toString() {
		return simulator+" "+socket;
	}
}
