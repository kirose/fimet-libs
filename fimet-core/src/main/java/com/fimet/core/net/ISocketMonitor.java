package com.fimet.core.net;

public interface ISocketMonitor {
	void onSocketDisconnected(ISocket socket);
	void onSocketConnecting(ISocket socket);
	void onSocketConnected(ISocket socket);
}
