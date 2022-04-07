package com.fimet.net;

import java.net.Socket;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.LoggerFactory;import org.slf4j.Logger;

import com.fimet.net.IESocket;
import com.fimet.net.ISocketListener;

/**
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 *
 */
public class AdaptedSocketClient extends AdaptedSocket {
	private static Logger logger = LoggerFactory.getLogger(AdaptedSocketClient.class);
	private AtomicBoolean connecting = new AtomicBoolean();
	public AdaptedSocketClient(IESocket pSocket, ISocketListener listener) {
		super(pSocket, listener);
	}
	@Override
	public boolean isServer() {
		return false;
	}
	@Override
	protected void connectSockets() {
		if (!connecting.get() && socketList.size() < maxOfConnections) {
			new ThreadConnector().start();
		}
	}
	private class ThreadConnector extends Thread {
		ThreadConnector(){
			super("SocketConnector "+AdaptedSocketClient.this);
		}
		public void run() {
			connecting.set(true);
			fireOnConnecting();
			Socket socket = null;
			while (alive && socket == null) {
				try {
					socket = new Socket(address, port);
					connecting.set(false);
					logger.debug("Connected "+ AdaptedSocketClient.this);
					SocketNode node = new SocketNode(socket);
					socketList.add(node);
					fireOnConnected();
					connectSockets();
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
