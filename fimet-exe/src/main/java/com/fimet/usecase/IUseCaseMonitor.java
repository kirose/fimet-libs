package com.fimet.usecase;

import com.fimet.exe.UseCaseResult;

public interface IUseCaseMonitor {
	void onUseCaseStart(IUseCase useCase);
	void onUseCaseFinish(IUseCase useCase, UseCaseResult results);
}
