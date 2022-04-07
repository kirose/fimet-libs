package com.fimet.net;

import com.fimet.net.ISocket;
import com.fimet.net.ISocketStore;

public class NullSocketStore implements ISocketStore {
	public static final ISocketStore INSTANCE = new NullSocketStore();
	@Override
	public void storeIncoming(ISocket socket, byte[] message) {}

	@Override
	public void storeOutgoing(ISocket socket, byte[] message) {}


}
