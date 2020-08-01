package com.fimet.socket;

public interface ISocketStore {
	void storeIncoming(ISocket socket, byte[] message);
	void storeOutgoing(ISocket socket, byte[] message);
}
