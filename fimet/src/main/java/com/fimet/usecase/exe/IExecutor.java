package com.fimet.usecase.exe;

import com.fimet.usecase.UseCase;

public interface IExecutor {
	void setListener(IExecutorListener listener);
	void startExecutor();
	void stopExecutor();
	void execute(UseCase useCase);
}
