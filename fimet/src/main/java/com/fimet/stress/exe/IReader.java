package com.fimet.stress.exe;

public interface IReader {
	void read(int numberOfMessages);
	void read();
	boolean canRead();
	boolean hasFinish();
	void stopRead();
	void startRead();
}
