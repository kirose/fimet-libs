package com.fimet.net;

import com.fimet.net.IConnectable;
import com.fimet.net.IMultiConnectable;

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
