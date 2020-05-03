package com.fimet.exe;

import java.io.File;

import com.fimet.usecase.UseCase;

public interface IUseCaseExecutor {
	void setListener(IUseCaseExecutorListener listener);
	void startExecutor();
	void stopExecutor();
	void setStore(IUseCaseStore store);
	void execute(UseCase useCase);
	void execute(File fileOrFolder);
}
