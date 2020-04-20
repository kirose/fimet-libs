package com.fimet.core.usecase.exe.sync;

import com.fimet.core.usecase.IUseCase;

interface IUseCaseTimerListener {
	void timeout(IUseCase useCase);
}
