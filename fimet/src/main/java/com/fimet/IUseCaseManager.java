package com.fimet;

import java.io.File;

import com.fimet.usecase.UseCase;
import com.fimet.usecase.exe.IExecutorListener;

public interface IUseCaseManager extends IManager {
	void execute(UseCase useCase);
	void setExcutorListener(IExecutorListener listener);
	void execute(UseCase useCase, IExecutorListener listener);
	void execute(File fileOrFolder);
	void execute(File fileOrFolder, IExecutorListener listener);
	void execute(String pathFileOrFolder);
	void execute(String pathFileOrFolder, IExecutorListener listener);
}
