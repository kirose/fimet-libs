package com.fimet.net;

public interface IConnectionListener {
	void onDisconnected(IConnectable connectable);
	void onConnecting(IConnectable connectable);
	void onConnected(IConnectable connectable);
}
