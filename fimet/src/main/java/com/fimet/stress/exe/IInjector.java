package com.fimet.stress.exe;

import com.fimet.net.ISocket;

public interface IInjector {
	void startInjector();
	void stopInjector();
	InjectorResult getResult();
	ISocket getSocket();
}
