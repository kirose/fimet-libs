package com.fimet.exe;

import com.fimet.usecase.IUseCase;
import com.fimet.usecase.exe.ExecutionResult;

public interface IUseCaseExecutorListener {
	void onStart(IUseCase useCase);
	void onFinish(IUseCase useCase, ExecutionResult results);
}
