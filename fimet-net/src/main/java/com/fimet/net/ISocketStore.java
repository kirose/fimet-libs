package com.fimet.net;

public interface ISocketStore {
	void storeIncoming(ISocket socket, byte[] message);
	void storeOutgoing(ISocket socket, byte[] message);
}
