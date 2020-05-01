package com.fimet.usecase.exe;

import com.fimet.usecase.IUseCase;

public interface IExecutorListener {
	void onStart(IUseCase useCase);
	void onFinish(IUseCase useCase, ExecutionResult results);
}
