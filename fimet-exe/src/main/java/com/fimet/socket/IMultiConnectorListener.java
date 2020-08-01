package com.fimet.socket;

public interface IMultiConnectorListener {
	void onConnectorConnect(IConnectable connectable);
	void onConnectorConnectAll(IMultiConnectable connectable);
	void onConnectorTimeout(IMultiConnectable connectable);
}
