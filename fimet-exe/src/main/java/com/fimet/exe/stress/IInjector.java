package com.fimet.exe.stress;

import com.fimet.exe.SocketResult;
import com.fimet.net.ISocket;
import com.fimet.simulator.ISimulator;

public interface IInjector {
	void startInjector();
	void stopInjector();
	SocketResult getResult();
	ISocket getSocket();
	ISimulator getSimulator();
}
