package com.fimet.exe;

import com.fimet.usecase.IUseCase;

public interface IUseCaseListener {
	void onUseCaseStart(IUseCase useCase);
	void onUseCaseFinish(IUseCase useCase);
}
