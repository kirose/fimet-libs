package com.fimet.net;

public interface IConnectable {
	void connect();
	void disconnect();
	IConnectionListener getConnectionListener();
	void setConnectionListener(IConnectionListener listener);
	boolean isDisconnected();
	boolean isConnected();
	boolean isConnecting();
}
