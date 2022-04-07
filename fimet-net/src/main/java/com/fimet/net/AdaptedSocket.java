package com.fimet.net;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.LoggerFactory;import org.slf4j.Logger;

import com.fimet.IAdapterManager;
import com.fimet.IEventManager;
import com.fimet.Manager;
import com.fimet.event.Event;
import com.fimet.net.IConnectable;
import com.fimet.net.IConnectionListener;
import com.fimet.net.IESocket;
import com.fimet.net.ISocket;
import com.fimet.net.ISocketListener;
import com.fimet.net.ISocketStore;
import com.fimet.net.NullConnectionListener;
import com.fimet.net.SocketException;
import com.fimet.parser.IStreamAdapter;
import com.fimet.utils.Args;
/**
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 *
 */
public abstract class AdaptedSocket implements ISocket, IConnectable {
	private static Logger logger = LoggerFactory.getLogger(AdaptedSocket.class);
	public static enum State {
		DISCONNECTED, CONNECTING, CONNECTED
	}
	public static int RECONNECTION_TIME = Manager.getPropertyInteger("socket.reconnectionTime", 2000);
	static IAdapterManager adapterManager = Manager.getManager(IAdapterManager.class);
	static IEventManager eventManager = Manager.getManager(IEventManager.class);

	protected String name;
	protected String address;
	protected int port;
	protected int maxOfConnections;
	//protected java.net.Socket socket;
	private boolean reconnect = true;
	protected boolean alive = true;
	private State state;
	private IStreamAdapter adapter;
	private ISocketListener listener;
	private IConnectionListener connectionListener;
	private AtomicLong numOfOutgoing = new AtomicLong(0);
	private AtomicLong numOfIncoming = new AtomicLong(0);
	private ISocketStore store;
	protected SocketList socketList;
	
	public AdaptedSocket(IESocket entity, ISocketListener listener) {
		super();
		Args.notNull("Socket Entity", entity);
		Args.notNull("Socket Name", entity.getName());
		Args.notNull("Socket Adapter", entity.getAdapter());
		Args.isIpAddress("Socket Address", entity.getAddress());
		Args.isPositive("Socket Port", entity.getPort());
		this.listener = listener != null ? listener : NullSocketListener.INSTANCE;
		if (listener instanceof IConnectionListener) {
			this.connectionListener = (IConnectionListener)listener; 
		} else {
			this.connectionListener = NullConnectionListener.INSTANCE;
		}
		this.name = entity.getName();
		this.address = entity.getAddress();
		this.port = entity.getPort();
		if (entity.getAdapter() == null)
			throw new NullPointerException("Socket Adapter is null");
		adapter = (IStreamAdapter)adapterManager.getAdapter(entity.getAdapter());
		reconnect = true;
		state = State.DISCONNECTED;
		store = NullSocketStore.INSTANCE;
		maxOfConnections = entity.getMaxConnections()!=null?entity.getMaxConnections():1;
		socketList = new SocketList(maxOfConnections);
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
	public void close() {
		socketList.removeAndCloseAll();
	}
	@Override
	public boolean isServer() {
		return this instanceof AdaptedSocketServer;
	}
	@Override
	public void connect() {
		if (isDisconnected()) {
			alive = true;
			connectSockets();
		}
	}
	@Override
	public void disconnect() {
		state = State.DISCONNECTED;
		alive = false;
		close();
		fireOnDisconnected();
	}
	public void setAutoReconnect(boolean reconnect) {
		this.reconnect = reconnect;
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
		return state == State.DISCONNECTED;
	}
	@Override
	public synchronized boolean isConnected() {
		return state == State.CONNECTED;
	}
	@Override
	public synchronized boolean isConnecting() {
		return state == State.CONNECTING;
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
		return name + (isServer()?" Server":" Client")+" "+address+" "+port;
	}
	@Override
	public void write(byte[] message) {
		write(message, true);
	}
	protected abstract void connectSockets();
	public int getNumOfConnections() {
		return socketList.size();
	}
	protected void fireOnConnected() {
		state = State.CONNECTED;
		try {
			connectionListener.onConnected(this);
			eventManager.fireEvent(Event.SOCKET_CONNECTED, this, this);
		} catch (Throwable e) {
			logger.error("Error Socket Connected Listener", e);
		}
	}
	protected void fireOnConnecting() {
		state = State.CONNECTING;
		try {
			connectionListener.onConnecting(this);
			eventManager.fireEvent(Event.SOCKET_CONNECTING, this, this);
		} catch (Throwable e) {
			logger.error("Error Socket Connecting Listener", e);
		}
	}
	protected void fireOnDisconnected() {
		state = State.DISCONNECTED;
		try {
			connectionListener.onDisconnected(this);
			eventManager.fireEvent(Event.SOCKET_DISCONNECTED, this, this);
		} catch (Throwable e) {
			logger.error("Error Socket Disconnected Listener "+AdaptedSocket.this, e);
		}
	}
	public void write(byte[] message, boolean adapt) {
		socketList.next().write(message, adapt);
	}
	protected class ThreadReader extends Thread {
		SocketNode node;
		ThreadReader(SocketNode node){
			super("SocketReader "+AdaptedSocket.this);
			Args.notNull("Socket Node", node);
			this.node = node;
		}
		public void run() {
			node.read();
		}
	}
	public class SocketNode {
		Socket socket;
		public SocketNode(Socket socket) {
			Args.notNull("Socket", socket);
			this.socket = socket;
			new ThreadReader(this).start();
		}
		public void read() {
			try {
				while(alive) {
					byte[] message = adapter.readStream(socket.getInputStream());
					if (logger.isDebugEnabled()) {
						logger.debug("Read from "+ AdaptedSocket.this+" ("+(message != null ? message.length : null)+" bytes)");
					}
					if (message == null || message.length == 0) {
						logger.debug("Socket port ocuppied "+AdaptedSocket.this);
						disconnect();
					} else {
						numOfIncoming.incrementAndGet();
						store.storeIncoming(AdaptedSocket.this, message);
						listener.onSocketRead(message);
					}
				}
			} catch (Exception e) {// Fallo la conexion
				if (!(e.getCause() instanceof java.net.SocketException && e.getCause().getMessage().contains("Socket closed"))) {
					logger.error("Socket Read Error ... disconnecting "+AdaptedSocket.this, e);
				}
				socketList.removeAndClose(this);// El servidor se desconecto no se puede leer del socket
				state = State.DISCONNECTED;
				fireOnDisconnected();
				if (alive && reconnect) {
					connectSockets();
				}
			}
		}
		public void write(byte[] message, boolean adapt) {
			try {
				if (socket == null) {
					throw new SocketException("Socket is disconnected cannot write data: "+AdaptedSocket.this);
				}
				store.storeOutgoing(AdaptedSocket.this, message);
				synchronized (this) {
					if (adapt) {
						adapter.writeStream(socket.getOutputStream(), message);
					} else {
						socket.getOutputStream().write(message);
					}
				}
				numOfOutgoing.incrementAndGet();
				if (logger.isDebugEnabled()) {
					logger.debug("Written to "+ AdaptedSocket.this+" ("+message.length+" bytes)");
				}
			} catch (IOException e) {
				logger.error("Socket write error  ("+message.length+" bytes), "+ AdaptedSocket.this, e);
				close();
				try {
					connectionListener.onDisconnected(AdaptedSocket.this);
					eventManager.fireEvent(Event.SOCKET_DISCONNECTED, AdaptedSocket.this, AdaptedSocket.this);
				} catch (Throwable ex) {
					logger.error("Error Socket Connected Listener", ex);
				}
				throw new SocketException(e);
			}
		}
		public void close() {
			if (socket!=null) {
				try {
					socket.close();
				} catch (Throwable e) {/*logger.warn("socket.close()", e);*/}
			}
		}
	}
	protected class SocketList {
		private SocketNode[] sockets;
		private int size;
		private int next;
		public SocketList(int maxOfConnections) {
			sockets = new SocketNode[maxOfConnections];
		}
		public void add(SocketNode socket) {
			if (size >= sockets.length)
				throw new ArrayIndexOutOfBoundsException(size+1);
			sockets[size++] = socket;
		}
		public void removeAndCloseAll() {
			if (size > 0) {
				for (int i = 0; i < sockets.length; i++) {
					sockets[i].close();
					sockets[i] = null;
				}
				size = 0;
			}
		}
		public int size() {
			return size;
		}
		protected void removeAndClose(SocketNode socket) {
			int index = indexOf(socket);
			if (index!=-1) {
				socket.close();
				if (size==1) {
					sockets[0] = null;
				} else {
					System.arraycopy(sockets, index+1, sockets, index, size-index);
				}
				size--;
			}
		}

		protected int indexOf(SocketNode socket) {
			if (size > 0) {
				for (int i = 0; i < sockets.length; i++) {
					if (sockets[i]==socket)
						return i;
				}
			}
			return -1;
		}
		private int nextIndex() {
			return (next = (next+1)%size);
		}
		public SocketNode next() {
			return size > 0 ? sockets[nextIndex()] : null;
		}
		public boolean isEmpty() {
			return size==0;
		}
	}
	public State getState() {
		return state;
	}
}
