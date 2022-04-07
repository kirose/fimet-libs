package com.fimet.net;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.LoggerFactory;import org.slf4j.Logger;

import com.fimet.net.IESocket;
import com.fimet.net.ISocketListener;
/**
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 *
 */
public class AdaptedSocketServer extends AdaptedSocket {
	private static Logger logger = LoggerFactory.getLogger(AdaptedSocketServer.class);
	protected ServerSocket serverSocket;
	private AtomicBoolean connecting = new AtomicBoolean();
	public AdaptedSocketServer(IESocket pSocket, ISocketListener listener) {
		super(pSocket, listener);
		newServer();
	}
	private void newServer() {
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			throw new SocketException("Socket error "+this,e);
		}
	}
	@Override
	public void close() {
		super.close();
		if (serverSocket != null) {
			try {
				serverSocket.close();
			} catch (Throwable e) {/*logger.warn("serverSocket.close()", e);*/}
			serverSocket = null;
		}
	}
	@Override
	public boolean isServer() {
		return true;
	}
	@Override
	protected void connectSockets() {
		if (socketList.size() < maxOfConnections) {
			if (serverSocket==null)
				newServer();
			new ThreadConnector().start();
		}
	}
	private class ThreadConnector extends Thread {
		ThreadConnector(){
			super("SocketConnector "+AdaptedSocketServer.this);
		}
		public void run() {
			fireOnConnecting();
			Socket socket = null;
			try {
				socket = serverSocket.accept();
				connecting.set(false);
				logger.debug("Connected "+ AdaptedSocketServer.this);
				SocketNode node = new SocketNode(socket);
				socketList.add(node);
				fireOnConnected();
				connectSockets();
			} catch (Exception e) {
				logger.error("Socket connection exception", e);
				fireOnDisconnected();
			}
		}
	}
}
