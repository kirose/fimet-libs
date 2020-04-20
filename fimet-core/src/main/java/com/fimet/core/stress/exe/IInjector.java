package com.fimet.core.stress.exe;

import com.fimet.core.net.ISocket;

public interface IInjector {
	ISocket getConnection();
	void enqueue(byte[] message);
	boolean canEnqueue();
	void onReaderInitilized(IReader reader);
	void onReaderFinish(IReader reader);
	void onReaderStart(IReader reader);
	long getNumberOfMessages();
	long getStartTime();
	long getFinishTime();
}
