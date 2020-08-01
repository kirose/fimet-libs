package com.fimet.socket;

public interface IConnectionListener {
	void onDisconnected(IConnectable connectable);
	void onConnecting(IConnectable connectable);
	void onConnected(IConnectable connectable);
}
