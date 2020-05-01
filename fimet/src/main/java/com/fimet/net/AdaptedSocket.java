package com.fimet.net;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import com.fimet.IPreferencesManager;
import com.fimet.ISocketManager;
import com.fimet.Manager;
import com.fimet.adapter.IStreamAdapter;
import com.fimet.commons.FimetLogger;
import com.fimet.commons.exception.SocketException;
/**
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 *
 */
public abstract class AdaptedSocket implements ISocket, IConnectable {

	public static enum Status {
		DISCONNECTED, CONNECTING, CONNECTED
	}
	static int RECONNECTION_TIME = Manager.get(IPreferencesManager.class).getInt(IPreferencesManager.SOCKET_RECONNECT_TIME_SEC,2)*1000;// In Miliseconds

	protected PSocket pSocket;
	protected java.net.Socket socket;
	private boolean reconnect = true;
	private boolean alive = true;
	private IStreamAdapter adapter;
	private ISocketListener listener;
	private IConnectionListener connectionListener;
	volatile private Status status;
	static SocketManager socketManager = (SocketManager)Manager.get(ISocketManager.class);
	private AtomicLong out = new AtomicLong(0);
	private AtomicLong in = new AtomicLong(0);
	public AdaptedSocket(PSocket pSocket) {
		this(pSocket, NullSocketListener.INSTANCE);
	}
	public AdaptedSocket(PSocket pSocket, ISocketListener listener) {
		super();
		if (pSocket == null)
			throw new NullPointerException();
		this.pSocket = pSocket;
		this.listener = listener != null ? listener : NullSocketListener.INSTANCE;
		if (listener instanceof IConnectionListener) {
			this.connectionListener = (IConnectionListener)listener; 
		} else {
			this.connectionListener = NullConnectionListener.INSTANCE;
		}
		if (pSocket.getAddress() == null) {
			throw new NullPointerException();
		} else if (!pSocket.getAddress().matches("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}")) {
			throw new SocketException("Invalid Address: " + pSocket.getAddress());
		}
		if (pSocket.getAddress() == null || pSocket.getPort() <= 0) {
			throw new SocketException("Invalid Port: " + pSocket.getPort());
		}
		if (pSocket.getAdapter() == null)
			throw new NullPointerException();
		adapter = (IStreamAdapter)pSocket.getAdapter();
		reconnect = false;
		status = Status.DISCONNECTED;
	}
	public IStreamAdapter getAdapter() {
		return adapter;
	}
	public void connect() {
		if (isDisconnected()) {
			new ThreadConnector().start();
		}
	}
	abstract protected java.net.Socket newSocket() throws IOException;
	public void disconnect() {
		status = Status.DISCONNECTED;
		alive = false;
		close();
		try {
			socketManager.onSocketDisconnected(this.pSocket);
			connectionListener.onDisconnected(this);
		} catch (Throwable e) {
			FimetLogger.error(AdaptedSocket.class, "Error Socket Disconnected Listener", e);
		}
	}
	abstract void close();
	public boolean isConncted() {
		return socket != null;
	}
	public void setConnectionListener(IConnectionListener listener) {
		connectionListener = listener != null ? listener : NullConnectionListener.INSTANCE;
	}
	public void setAutoReconnect(boolean reconnect) {
		this.reconnect = reconnect;
	}
	public InputStream getInputStream() throws IOException {
		return socket.getInputStream();
	}
	public synchronized boolean isDisconnected() {
		return status == Status.DISCONNECTED;
	}
	public synchronized boolean isConnected() {
		return status == Status.CONNECTED;
	}
	public synchronized boolean isConnecting() {
		return status == Status.CONNECTING;
	}
	@Override
	public PSocket getParameters() {
		return pSocket;
	}
	@Override
	public long getNumOfRead() {
		return in.get();
	}
	@Override
	public long getNumOfWrite() {
		return out.get();
	}
	@Override
	public int hashCode() {
		return pSocket.hashCode();
	}
	@Override
	public Integer getPort() {
		return pSocket.getPort();
	}
	@Override
	public String getAddress() {
		return pSocket.getAddress();
	}
	@Override
	public String toString() {
		return pSocket.toString();
	}
	public void write(byte[] message) {
		write(message, true);
	}
	public void write(byte[] message, boolean adapt) {
		try {
			if (socket == null) {
				throw new SocketException("Socket is disconnected cannot write data: "+pSocket);
			}
			synchronized (this) {
				if (adapt) {
					adapter.writeStream(socket.getOutputStream(), message);
				} else {
					socket.getOutputStream().write(message);
				}
			}
			out.incrementAndGet();
			if (FimetLogger.isEnabledDebug()) {
				FimetLogger.debug(AdaptedSocket.class,"Written message ("+message.length+" bytes) to "+ this);
			}
		} catch (IOException e) {
			FimetLogger.error("Fail to write message ("+message.length+" bytes) to "+ this, e);
			close();
			try {
				socketManager.onSocketDisconnected(this.pSocket);
				connectionListener.onDisconnected(this);
			} catch (Throwable ex) {
				FimetLogger.error(AdaptedSocket.class, "Error Socket Connected Listener", ex);
			}
			throw new SocketException(e);
		}
	}
	private class ThreadReader extends Thread {
		ThreadReader(){
			setName("SocketReader "+AdaptedSocket.this);
		}
		public void run() {
			try {
				while(alive){
					byte[] message = adapter.readStream(socket.getInputStream());
					if (FimetLogger.isEnabledDebug()) {
						FimetLogger.debug(AdaptedSocket.class,"Read message ("+(message != null ? message.length : null)+" bytes) from "+ AdaptedSocket.this);
					}
					if (message == null || message.length == 0) {
						FimetLogger.debug(AdaptedSocket.class, "Socket port ocuppied: "+AdaptedSocket.this);
						disconnect();
					} else {
						in.incrementAndGet();
						listener.onSocketRead(message);
					}
				}
			} catch (Exception e) {// Fallo la conexion
				FimetLogger.error("Socket Read Error ... disconnecting", e);
				close();// El servidor se desconecto no se puede leer del socket
				status = Status.DISCONNECTED;
				try {
					socketManager.onSocketDisconnected(AdaptedSocket.this.pSocket);
					connectionListener.onDisconnected(AdaptedSocket.this);
				} catch (Throwable ex) {
					FimetLogger.error(AdaptedSocket.class, "Error Socket Connected Listener", ex);
				}
				if (alive && reconnect) {
					new ThreadConnector().start();
				}
			}
		}
	}
	private class ThreadConnector extends Thread {
		ThreadConnector(){
			setName("SocketConnector "+AdaptedSocket.this);
			status = Status.CONNECTING;
		}
		public void run() {
			try {
				socketManager.onSocketConnecting(AdaptedSocket.this.pSocket);
				connectionListener.onConnecting(AdaptedSocket.this);
			} catch (Throwable e) {
				FimetLogger.error(AdaptedSocket.class, "Error Socket Connecting Listener", e);
			}
			while (alive && socket == null) {
				try {
					socket = newSocket();
					status = Status.CONNECTED;
					FimetLogger.debug(AdaptedSocket.class,"Connected to "+ AdaptedSocket.this);
					new ThreadReader().start();
				} catch (Exception e) {
					socket = null;
				}
				try {
					socketManager.onSocketConnected(AdaptedSocket.this.pSocket);
					connectionListener.onConnected(AdaptedSocket.this);
				} catch (Throwable e) {
					FimetLogger.error(AdaptedSocket.class, "Error Socket Connected Listener", e);
				}
				if (RECONNECTION_TIME <= 0) {
					disconnect();
				} else {
					try {
						TimeUnit.MILLISECONDS.sleep(RECONNECTION_TIME);
					} catch (Exception e) {}
				}
			}
		}
	}
}
