package com.fimet.net;

import com.fimet.net.ISocketListener;

public class NullSocketListener implements ISocketListener {
	public static final ISocketListener INSTANCE = new NullSocketListener();
	@Override
	public void onSocketRead(byte[] bytes) {
	}
}
