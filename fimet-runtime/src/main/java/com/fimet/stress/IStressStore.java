package com.fimet.stress;

import java.util.UUID;

import com.fimet.exe.SocketResult;

public interface IStressStore {
	void open(UUID idTask);
	void close(UUID idTask);
	void storeCycleResults(SocketResult result);
	void storeGlobalResults(SocketResult result);
}
