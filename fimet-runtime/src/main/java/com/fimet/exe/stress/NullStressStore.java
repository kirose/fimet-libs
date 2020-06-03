package com.fimet.exe.stress;

import java.util.UUID;

import com.fimet.exe.SocketResult;
import com.fimet.stress.IStressStore;

public class NullStressStore implements IStressStore {
	public static final IStressStore INSTANCE = new NullStressStore();

	@Override
	public void storeCycleResults(SocketResult result) {}

	@Override
	public void storeGlobalResults(SocketResult result) {}

	@Override
	public void open(UUID idTask) {}

	@Override
	public void close(UUID idTask) {}

}
