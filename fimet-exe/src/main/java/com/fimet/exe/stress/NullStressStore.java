package com.fimet.exe.stress;

import com.fimet.exe.SocketResult;
import com.fimet.exe.Task;
import com.fimet.stress.IStressStore;

public class NullStressStore implements IStressStore {
	public static final IStressStore INSTANCE = new NullStressStore();

	@Override
	public void storeCycleResults(SocketResult result) {}

	@Override
	public void storeGlobalResults(SocketResult result) {}

	@Override
	public void init(Task task, Object... params) {}

	@Override
	public void close() {}

	@Override
	public void save() {
	}

}
