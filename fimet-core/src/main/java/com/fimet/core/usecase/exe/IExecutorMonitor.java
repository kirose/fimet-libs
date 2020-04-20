package com.fimet.core.usecase.exe;

import com.fimet.core.usecase.IUseCase;

public interface IExecutorMonitor {
	void onStart(IUseCase useCase);
	void onFinish(IUseCase useCase, ExecutionResult data);
}
