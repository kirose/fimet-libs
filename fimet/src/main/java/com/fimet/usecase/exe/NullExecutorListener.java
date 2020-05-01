package com.fimet.usecase.exe;

import com.fimet.usecase.IUseCase;

public class NullExecutorListener implements IExecutorListener {
	public static final IExecutorListener INSTANCE = new NullExecutorListener();
	@Override
	public void onStart(IUseCase useCase) {}
	@Override
	public void onFinish(IUseCase useCase, ExecutionResult result) {}
}
