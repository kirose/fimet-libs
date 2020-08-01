package com.fimet.socket;

import java.io.IOException;
import java.net.Socket;

/**
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 *
 */
public class AdaptedSocketClient extends AdaptedSocket {

	public AdaptedSocketClient(IESocket pSocket, ISocketListener listener) {
		super(pSocket, listener);
	}
	@Override
	protected Socket newSocket() throws IOException {
		return socket = new Socket(address, port);
	}
	@Override
	void close() {
		if (socket != null)  {
			try {
				socket.close();
			} catch (Throwable e) {/*FimetLogger.warning("socket.close()", e);*/}
			socket = null;
		}
	}
	@Override
	public boolean isServer() {
		return false;
	}
}
