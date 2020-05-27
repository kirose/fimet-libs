package com.fimet.usecase;


import com.fimet.exe.IExecutorListener;
import com.fimet.exe.Task;

public interface IUseCaseExecutor {
	void setListener(IUseCaseExecutorListener listener);
	void setStore(IUseCaseStore store);
	void execute(Task task);
	void cancel();
	void setExecutorListener(IExecutorListener listener);
}
