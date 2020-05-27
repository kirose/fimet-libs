package com.fimet.exe.usecase;

import com.fimet.exe.UseCaseResult;
import com.fimet.usecase.IUseCase;
import com.fimet.usecase.IUseCaseExecutorListener;

public class NullUseCaseExecutorListener implements IUseCaseExecutorListener {
	public static final IUseCaseExecutorListener INSTANCE = new NullUseCaseExecutorListener();
	@Override
	public void onStart(IUseCase useCase) {}
	@Override
	public void onFinish(IUseCase useCase, UseCaseResult result) {}
}
