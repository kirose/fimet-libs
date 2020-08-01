package com.fimet.exe.usecase;

import com.fimet.exe.UseCaseResult;
import com.fimet.usecase.IUseCase;
import com.fimet.usecase.IUseCaseMonitor;

public class NullUseCaseMonitor implements IUseCaseMonitor {
	public static final IUseCaseMonitor INSTANCE = new NullUseCaseMonitor();
	@Override
	public void onUseCaseStart(IUseCase useCase) {}
	@Override
	public void onUseCaseFinish(IUseCase useCase, UseCaseResult result) {}
}
