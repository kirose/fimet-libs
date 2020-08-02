package com.fimet.net;

import com.fimet.net.ISocket;
import com.fimet.net.ISocketStore;

public class SocketStoreWrapper implements ISocketStore {
	volatile private ISocketStore wrapped;
	public SocketStoreWrapper(ISocketStore wrapped) {
		this.wrapped = wrapped;
	}
	
	public ISocketStore getWrapped() {
		return wrapped;
	}

	public void setWrapped(ISocketStore wrapped) {
		this.wrapped = wrapped!=null?wrapped:NullSocketStore.INSTANCE;
	}

	@Override
	public void storeIncoming(ISocket socket, byte[] message) {
		wrapped.storeIncoming(socket, message);
	}

	@Override
	public void storeOutgoing(ISocket socket, byte[] message) {
		wrapped.storeOutgoing(socket, message);
	}
}
