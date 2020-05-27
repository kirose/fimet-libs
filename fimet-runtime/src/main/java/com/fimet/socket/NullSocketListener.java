package com.fimet.socket;

public class NullSocketListener implements ISocketListener {
	public static final ISocketListener INSTANCE = new NullSocketListener();
	@Override
	public void onSocketRead(byte[] bytes) {
	}
}
