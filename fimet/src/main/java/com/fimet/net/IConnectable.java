package com.fimet.net;

public interface IConnectable {
	void connect();
	void disconnect();
	void setConnectionListener(IConnectionListener listener);
	boolean isDisconnected();
	boolean isConnected();
	boolean isConnecting();
}
