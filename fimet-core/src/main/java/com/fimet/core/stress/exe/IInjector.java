package com.fimet.core.stress.exe;

import com.fimet.core.net.IMessenger;

public interface IInjector {
	void startInjector();
	void stopInjector();
	InjectorResult getResult();
	IMessenger getMessenger();
}
