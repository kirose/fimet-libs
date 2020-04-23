package com.fimet.core.stress.exe;

import com.fimet.core.net.IMessenger;

public interface IInjector {
	boolean isFinished();
	void startInjector();
	void stopInjector();
	MessengerResult getMessengerResult();
	InjectorResult getResult();
	IMessenger getMessenger();
	String getStatsCycle();
}
