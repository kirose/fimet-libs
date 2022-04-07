package com.fimet.net;

import com.fimet.net.IConnectable;
import com.fimet.net.IMultiConnectable;

public interface IMultiConnectorListener {
	void onConnectorConnect(IConnectable connectable);
	void onConnectorConnectAll(IMultiConnectable connectable);
	void onConnectorTimeout(IMultiConnectable connectable);
}
