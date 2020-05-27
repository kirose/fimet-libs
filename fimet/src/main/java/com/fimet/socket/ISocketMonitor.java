package com.fimet.socket;

public interface ISocketMonitor {
	void onSocketDisconnected(PSocket socket);
	void onSocketConnecting(PSocket socket);
	void onSocketConnected(PSocket socket);
}
