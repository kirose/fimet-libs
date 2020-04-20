package com.fimet.core.stress.exe;

public interface IReader {
	void read();
	boolean canRead();
	void stopRead();
	void startRead();
	long getNumberOfWaits();
	long getNumberOfMessages();
	long getStartTime();
	long getFinishTime();
}
