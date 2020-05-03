package com.fimet.stress.exe;

import com.fimet.exe.IStressStore;

public class NullStressStore implements IStressStore {
	public static final IStressStore INSTANCE = new NullStressStore();
	@Override
	public void storeCycleResults(InjectorResult result) {}

	@Override
	public void storeGlobalResults(InjectorResult result) {}

	@Override
	public void close() {}

}
