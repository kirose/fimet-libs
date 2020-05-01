package com.fimet.stress.exe;

public interface IStoreResults {
	void close();
	void storeCycleResults(InjectorResult result);
	void storeGlobalResults(InjectorResult result);
}
