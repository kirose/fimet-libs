package com.fimet.exe.stress;

import com.fimet.exe.InjectorResult;
import com.fimet.simulator.ISimulator;
import com.fimet.socket.ISocket;

public interface IInjector {
	void startInjector();
	void stopInjector();
	InjectorResult getResult();
	ISocket getSocket();
	ISimulator getSimulator();
}
