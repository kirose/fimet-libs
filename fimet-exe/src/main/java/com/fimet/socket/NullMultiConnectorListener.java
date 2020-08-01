package com.fimet.socket;

import com.fimet.socket.IMultiConnectable;

public class NullMultiConnectorListener implements IMultiConnectorListener {
	public static final IMultiConnectorListener INSTANCE = new NullMultiConnectorListener();

	@Override
	public void onConnectorConnectAll(IMultiConnectable connectable) {
	}

	@Override
	public void onConnectorConnect(IConnectable simulator) {
	}

	@Override
	public void onConnectorTimeout(IMultiConnectable connectable) {
	}

}
