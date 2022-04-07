package com.fimet;

import java.util.List;

import com.fimet.net.IESocket;
import com.fimet.net.ISocket;
import com.fimet.net.ISocketListener;
import com.fimet.net.ISocketStore;
import com.fimet.net.SocketException;
/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface ISocketManager extends IManager {
	ISocket getSocket(String name);
	ISocket getSocket(String name, ISocketListener listener);
	ISocket getSocket(IESocket socket);
	ISocket getSocket(IESocket socket, ISocketListener listener);
	ISocket connect(IESocket socket, ISocketListener listener) throws SocketException;
	void disconnect(String name);
	void disconnectAll();
	List<ISocket> getSockets();
	ISocket remove(String name);
	ISocket reload(String name);
	void setSocketTimeReconnect(int sec);
	void setStore(ISocketStore store);
}
