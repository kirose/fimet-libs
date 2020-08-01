package com.fimet.exe.stress;

import com.fimet.exe.SocketResult;
import com.fimet.simulator.ISimulator;
import com.fimet.socket.ISocket;

public interface IInjector {
	void startInjector();
	void stopInjector();
	SocketResult getResult();
	ISocket getSocket();
	ISimulator getSimulator();
}
