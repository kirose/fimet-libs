package com.fimet.stress.exe;


public interface ITimer {
	boolean hasFinish();
	void startTimer();
	void stopTimer();
	IReader getReader();
	IInjector getInjector();
}
