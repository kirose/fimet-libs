package com.fimet.net;


import java.io.IOException;

import java.net.Socket;

import com.fimet.net.IESocket;
import com.fimet.net.ISocketListener;
/**
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 *
 */
public class AdaptedSocketServer extends AdaptedSocket {

	protected java.net.ServerSocket serverSocket;
	
	public AdaptedSocketServer(IESocket pSocket, ISocketListener listener) {
		super(pSocket, listener);
	}
	
	@Override
	protected Socket newSocket() throws IOException {
		serverSocket = new java.net.ServerSocket(port);
		return socket = serverSocket.accept();
	}
	@Override
	void close() {
		if (socket != null)  {
			try {
				socket.close();
			} catch (Throwable e) {/*FimetLogger.warning("socket.close()", e);*/}
			socket = null;
		}
		if (serverSocket != null) {
			try {
				serverSocket.close();
			} catch (Throwable e) {/*FimetLogger.warning("serverSocket.close()", e);*/}
			serverSocket = null;
		}
	}
	@Override
	public boolean isServer() {
		return true;
	}

}
