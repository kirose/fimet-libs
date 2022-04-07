package com.fimet.exe.stress;

public interface ITimer {
	boolean hasFinish();
	void startTimer();
	void stopTimer();
	IReader getReader();
	IInjector getInjector();
}
