package com.fimet;

import java.io.File;

import com.fimet.exe.IStressExecutor;
import com.fimet.exe.IStressExecutorListener;
import com.fimet.exe.IUseCaseExecutor;
import com.fimet.exe.IUseCaseExecutorListener;
import com.fimet.stress.Stress;
import com.fimet.usecase.UseCase;

public interface IExecutorManager extends IManager {
	void execute(UseCase useCase);
	void execute(UseCase useCase, IUseCaseExecutorListener listener);
	void execute(File fileOrFolderUseCase);
	void execute(File fileOrFolderUseCase, IUseCaseExecutorListener listener);
	void execute(String pathFileOrFolderUseCase);
	void execute(String pathFileOrFolderUseCase, IUseCaseExecutorListener listener);
	void execute(Stress stress, IStressExecutorListener listener);
	void execute(Stress stress);
	IStressExecutor getStressExecutor();
	IUseCaseExecutor getUseCaseExecutor();
}
