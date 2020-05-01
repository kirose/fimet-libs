package com.fimet.net;

import com.fimet.net.MultiConnector.IMultiConnectable;
import com.fimet.net.MultiConnector.IMultiConnectorListener;

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
