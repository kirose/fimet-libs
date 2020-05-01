package com.fimet.stress.exe;

public class NullStoreResults implements IStoreResults {
	public static final IStoreResults INSTANCE = new NullStoreResults();
	@Override
	public void storeCycleResults(InjectorResult result) {}

	@Override
	public void storeGlobalResults(InjectorResult result) {}

	@Override
	public void close() {}

}
