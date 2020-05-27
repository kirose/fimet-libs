package com.fimet.stress;

import com.fimet.exe.IExecutorListener;
import com.fimet.exe.Task;

public interface IStressExecutor {
	void execute(Task stress);
	void setListener(IStressExecutorListener listener);
	void setStore(IStressStore store);
	void setExecutorListener(IExecutorListener executorManager);
	void cancel();
}
