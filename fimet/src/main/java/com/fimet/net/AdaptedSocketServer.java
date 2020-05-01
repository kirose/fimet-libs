package com.fimet.net;


import java.io.IOException;

import java.net.Socket;
/**
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 *
 */
public class AdaptedSocketServer extends AdaptedSocket {

	protected java.net.ServerSocket serverSocket;
	
	public AdaptedSocketServer(PSocket pSocket, ISocketListener listener) {
		super(pSocket, listener);
	}
	
	@Override
	protected Socket newSocket() throws IOException {
		serverSocket = new java.net.ServerSocket(pSocket.getPort());
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
}
