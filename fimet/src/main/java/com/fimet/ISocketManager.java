package com.fimet;

import com.fimet.socket.ISocket;
import com.fimet.socket.ISocketListener;
import com.fimet.socket.ISocketMonitor;
import com.fimet.socket.ISocketStore;
import com.fimet.socket.PSocket;
import com.fimet.socket.SocketException;
/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface ISocketManager extends IManager {
	ISocket getSocket(PSocket socket);
	ISocket getSocket(PSocket socket, ISocketListener listener);
	ISocket connect(PSocket socket, ISocketListener listener) throws SocketException;
	void disconnect(PSocket socket);
	void disconnectAll();
	void setSocketTimeReconnect(int sec);
	void addMonitor(ISocketMonitor monitor);
	void removeMonitor(ISocketMonitor monitor);
	void setStore(ISocketStore store);
}
