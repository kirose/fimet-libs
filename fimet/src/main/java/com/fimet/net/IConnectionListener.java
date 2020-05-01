package com.fimet.net;

public interface IConnectionListener {
	void onDisconnected(IConnectable connection);
	void onConnecting(IConnectable connection);
	void onConnected(IConnectable connection);
}
