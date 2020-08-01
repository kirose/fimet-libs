package com.fimet.socket;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import com.fimet.FimetLogger;
import com.fimet.IAdapterManager;
import com.fimet.IEventManager;
import com.fimet.Manager;
import com.fimet.event.Event;
import com.fimet.parser.IStreamAdapter;
import com.fimet.utils.Args;
/**
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 *
 */
public abstract class AdaptedSocket implements ISocket, IConnectable {

	public static enum Status {
		DISCONNECTED, CONNECTING, CONNECTED
	}
	public static int RECONNECTION_TIME = Manager.getPropertyInteger("socket.reconnectionTime", 2000);
	static IAdapterManager adapterManager = Manager.get(IAdapterManager.class);
	static IEventManager eventManager = Manager.get(IEventManager.class);

	protected String name;
	protected String address;
	protected int port;
	protected java.net.Socket socket;
	private boolean reconnect = true;
	private boolean alive = true;
	volatile private Status status;
	private IStreamAdapter adapter;
	private ISocketListener listener;
	private IConnectionListener connectionListener;
	private AtomicLong numOfOutgoing = new AtomicLong(0);
	private AtomicLong numOfIncoming = new AtomicLong(0);
	private ISocketStore store;
	
	public AdaptedSocket(IESocket pSocket, ISocketListener listener) {
		super();
		Args.notNull("Socket Entity", pSocket);
		Args.notNull("Socket Name", pSocket.getName());
		Args.notNull("Socket Adapter", pSocket.getAdapter());
		Args.isIpAddress("Socket Address", pSocket.getAddress());
		Args.isPositive("Socket Port", pSocket.getPort());
		this.listener = listener != null ? listener : NullSocketListener.INSTANCE;
		if (listener instanceof IConnectionListener) {
			this.connectionListener = (IConnectionListener)listener; 
		} else {
			this.connectionListener = NullConnectionListener.INSTANCE;
		}
		this.name = pSocket.getName();
		this.address = pSocket.getAddress();
		this.port = pSocket.getPort();
		if (pSocket.getAdapter() == null)
			throw new NullPointerException("Socket Adapter is null");
		adapter = (IStreamAdapter)adapterManager.getAdapter(pSocket.getAdapter());
		reconnect = false;
		status = Status.DISCONNECTED;
		store = NullSocketStore.INSTANCE;
	}
	public String getName() {
		return name;
	}
	@Override
	public IStreamAdapter getAdapter() {
		return adapter;
	}
	public void setStore(ISocketStore store) {
		this.store = store!=null?store:NullSocketStore.INSTANCE;
	}
	@Override
	public void setListener(ISocketListener listener) {
		this.listener = listener;
	}
	abstract void close();
	abstract protected java.net.Socket newSocket() throws IOException;
	@Override
	public boolean isServer() {
		return this instanceof AdaptedSocketServer;
	}
	@Override
	public void connect() {
		if (isDisconnected()) {
			alive = true;
			new ThreadConnector().start();
		}
	}
	@Override
	public void disconnect() {
		status = Status.DISCONNECTED;
		alive = false;
		close();
		try {
			connectionListener.onDisconnected(this);
			eventManager.fireEvent(Event.SOCKET_DISCONNECTED, this, this);
		} catch (Throwable e) {
			FimetLogger.error(AdaptedSocket.class, "Error Socket Disconnected Listener "+AdaptedSocket.this, e);
		}
	}
	public void setAutoReconnect(boolean reconnect) {
		this.reconnect = reconnect;
	}
	public InputStream getInputStream() throws IOException {
		return socket.getInputStream();
	}
	@Override
	public void setConnectionListener(IConnectionListener listener) {
		connectionListener = listener != null ? listener : NullConnectionListener.INSTANCE;
	}
	@Override
	public IConnectionListener getConnectionListener() {
		return connectionListener;
	}
	@Override
	public synchronized boolean isDisconnected() {
		return status == Status.DISCONNECTED;
	}
	@Override
	public synchronized boolean isConnected() {
		return status == Status.CONNECTED;
	}
	@Override
	public synchronized boolean isConnecting() {
		return status == Status.CONNECTING;
	}
	@Override
	public long getNumOfRead() {
		return numOfIncoming.get();
	}
	@Override
	public long getNumOfWrite() {
		return numOfOutgoing.get();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + (Integer.hashCode(port));
		result = prime * result + (isServer() ? 1231 : 1237);
		return result;
	}
	@Override
	public Integer getPort() {
		return port;
	}
	@Override
	public String getAddress() {
		return address;
	}
	@Override
	public String toString() {
		return name + (isServer()?"Server":"Client")+" "+address+" "+port;
	}
	@Override
	public void write(byte[] message) {
		write(message, true);
	}
	@Override
	public void write(byte[] message, boolean adapt) {
		try {
			if (socket == null) {
				throw new SocketException("Socket is disconnected cannot write data: "+this);
			}
			store.storeOutgoing(this, message);
			synchronized (this) {
				if (adapt) {
					adapter.writeStream(socket.getOutputStream(), message);
				} else {
					socket.getOutputStream().write(message);
				}
			}
			numOfOutgoing.incrementAndGet();
			if (FimetLogger.isEnabledDebug()) {
				FimetLogger.debug(AdaptedSocket.class,"Written to "+ this+" ("+message.length+" bytes)");
			}
		} catch (IOException e) {
			FimetLogger.error(AdaptedSocket.class, "Socket write error  ("+message.length+" bytes), "+ this, e);
			close();
			try {
				connectionListener.onDisconnected(this);
				eventManager.fireEvent(Event.SOCKET_DISCONNECTED, this, this);
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
				while(alive) {
					byte[] message = adapter.readStream(socket.getInputStream());
					if (FimetLogger.isEnabledDebug()) {
						FimetLogger.debug(AdaptedSocket.class,"Read from "+ AdaptedSocket.this+" ("+(message != null ? message.length : null)+" bytes)");
					}
					if (message == null || message.length == 0) {
						FimetLogger.debug(AdaptedSocket.class, "Socket port ocuppied "+AdaptedSocket.this);
						disconnect();
					} else {
						numOfIncoming.incrementAndGet();
						store.storeIncoming(AdaptedSocket.this, message);
						listener.onSocketRead(message);
					}
				}
			} catch (Exception e) {// Fallo la conexion
				FimetLogger.error(AdaptedSocket.class,"Socket Read Error ... disconnecting "+AdaptedSocket.this, e);
				close();// El servidor se desconecto no se puede leer del socket
				status = Status.DISCONNECTED;
				try {
					connectionListener.onDisconnected(AdaptedSocket.this);
					eventManager.fireEvent(Event.SOCKET_DISCONNECTED, AdaptedSocket.this, AdaptedSocket.this);
				} catch (Throwable ex) {
					FimetLogger.error(AdaptedSocket.class, "Error Socket Connected Listener "+AdaptedSocket.this, ex);
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
				connectionListener.onConnecting(AdaptedSocket.this);
				eventManager.fireEvent(Event.SOCKET_CONNECTING, AdaptedSocket.this, AdaptedSocket.this);
			} catch (Throwable e) {
				FimetLogger.error(AdaptedSocket.class, "Error Socket Connecting Listener", e);
			}
			while (alive && socket == null) {
				try {
					socket = newSocket();
					status = Status.CONNECTED;
					FimetLogger.debug(AdaptedSocket.class,"Connected "+ AdaptedSocket.this);
					new ThreadReader().start();
					try {
						connectionListener.onConnected(AdaptedSocket.this);
						eventManager.fireEvent(Event.SOCKET_CONNECTED, AdaptedSocket.this, AdaptedSocket.this);
					} catch (Throwable e) {
						FimetLogger.error(AdaptedSocket.class, "Error Socket Connected Listener", e);
					}
				} catch (Exception e) {
					socket = null;
					if (RECONNECTION_TIME <= 0) {
						disconnect();
					} else {
						try {
							TimeUnit.MILLISECONDS.sleep(RECONNECTION_TIME);
						} catch (Exception ex) {}
					}
				}
			}
		}
	}
}
