package com.fimet.usecase;

import com.fimet.exe.UseCaseResult;

public interface IUseCaseExecutorListener {
	void onStart(IUseCase useCase);
	void onFinish(IUseCase useCase, UseCaseResult results);
}
