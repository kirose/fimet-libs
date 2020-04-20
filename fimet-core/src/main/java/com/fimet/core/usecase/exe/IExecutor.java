package com.fimet.core.usecase.exe;

import com.fimet.core.usecase.IUseCase;

public interface IExecutor {
	void removeMonitor();
	void setMonitor(IExecutorMonitor monitor);
	void startExecutor();
	void stopExecutor();
	void execute(IUseCase useCase);
}
