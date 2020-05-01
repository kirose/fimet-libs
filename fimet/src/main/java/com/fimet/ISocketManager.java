package com.fimet;

import com.fimet.net.ISocket;
import com.fimet.net.ISocketListener;
import com.fimet.net.ISocketMonitor;
import com.fimet.net.PSocket;

public interface ISocketManager extends IManager {
	ISocket getSocket(PSocket socket);
	ISocket getSocket(PSocket socket, ISocketListener listener);
	ISocket connect(PSocket socket, ISocketListener listener);
	void disconnect(PSocket socket);
	void disconnectAll();
	void setSocketTimeReconnect(int sec);
	void addMonitor(ISocketMonitor monitor);
	void removeMonitor(ISocketMonitor monitor);
}
