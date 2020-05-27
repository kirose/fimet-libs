package com.fimet.exe.stress;

import java.util.UUID;

import com.fimet.exe.InjectorResult;
import com.fimet.stress.IStressStore;

public class NullStressStore implements IStressStore {
	public static final IStressStore INSTANCE = new NullStressStore();

	@Override
	public void storeCycleResults(InjectorResult result) {}

	@Override
	public void storeGlobalResults(InjectorResult result) {}

	@Override
	public void open(UUID idTask) {}

	@Override
	public void close(UUID idTask) {}

}
