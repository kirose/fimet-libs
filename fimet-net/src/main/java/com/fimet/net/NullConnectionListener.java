package com.fimet.net;

public class NullConnectionListener implements IConnectionListener {

	public static final IConnectionListener INSTANCE = new NullConnectionListener();

	@Override
	public void onDisconnected(IConnectable connection) {
	}

	@Override
	public void onConnecting(IConnectable connection) {
	}

	@Override
	public void onConnected(IConnectable connection) {
	}


}
