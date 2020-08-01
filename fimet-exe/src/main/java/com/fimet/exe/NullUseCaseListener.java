package com.fimet.exe;

import com.fimet.usecase.IUseCase;

public class NullUseCaseListener implements IUseCaseListener {
	public static final NullUseCaseListener INSTANCE = new NullUseCaseListener();

	@Override
	public void onUseCaseStart(IUseCase useCase) {
	}

	@Override
	public void onUseCaseFinish(IUseCase useCase) {
	}
}
