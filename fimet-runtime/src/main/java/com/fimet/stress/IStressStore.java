package com.fimet.stress;

import java.util.UUID;

import com.fimet.exe.InjectorResult;

public interface IStressStore {
	void open(UUID idTask);
	void close(UUID idTask);
	void storeCycleResults(InjectorResult result);
	void storeGlobalResults(InjectorResult result);
}
