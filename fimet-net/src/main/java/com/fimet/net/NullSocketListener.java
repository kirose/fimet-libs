package com.fimet.net;

import com.fimet.core.net.IAdaptedSocketListener;

public class NullSocketListener implements IAdaptedSocketListener {
	public static final IAdaptedSocketListener INSTANCE = new NullSocketListener();
	@Override
	public void onSocketRead(byte[] bytes) {
	}

	@Override
	public void onSocketDisconnected() {
	}

	@Override
	public void onSocketConnecting() {
	}

	@Override
	public void onSocketConnected() {
	}

}
