package com.fimet.exe;

import com.fimet.stress.exe.InjectorResult;

public interface IStressStore {
	void close();
	void storeCycleResults(InjectorResult result);
	void storeGlobalResults(InjectorResult result);
}
