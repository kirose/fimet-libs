package com.fimet.usecase.exe;

import com.fimet.exe.IUseCaseExecutorListener;
import com.fimet.usecase.IUseCase;

public class NullUseCaseExecutorListener implements IUseCaseExecutorListener {
	public static final IUseCaseExecutorListener INSTANCE = new NullUseCaseExecutorListener();
	@Override
	public void onStart(IUseCase useCase) {}
	@Override
	public void onFinish(IUseCase useCase, ExecutionResult result) {}
}
