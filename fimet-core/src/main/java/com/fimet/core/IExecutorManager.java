package com.fimet.core;

import com.fimet.core.usecase.exe.IExecutor;

public interface IExecutorManager extends IManager {
	IExecutor getSyncExecutor();
	IExecutor getAsyncExecutor();
}
