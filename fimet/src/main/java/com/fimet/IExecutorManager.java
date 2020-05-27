package com.fimet;

import java.io.File;

import com.fimet.exe.ExecutionException;
import com.fimet.exe.IExecutorListener;
import com.fimet.exe.Task;
import com.fimet.stress.IStress;
import com.fimet.stress.IStressExecutorListener;
import com.fimet.usecase.IUseCase;
import com.fimet.usecase.IUseCaseExecutorListener;
/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface IExecutorManager extends IManager {
	void setExecutorListener(IExecutorListener listener);
	Task execute(Task task) throws ExecutionException;
	Task executeUseCase(Object useCase) throws ExecutionException;
	Task execute(IUseCase useCase) throws ExecutionException;
	Task execute(File fileOrFolderUseCase) throws ExecutionException;
	Task execute(String pathFileOrFolderUseCase) throws ExecutionException;
	Task executeStress(Object stress) throws ExecutionException;
	Task execute(IStress stress) throws ExecutionException;
	void setUseCaseExecutorListener(IUseCaseExecutorListener listener);
	void setStressExecutorListener(IStressExecutorListener listener);
	void cancel(Task task);
	Task getRunningTask();
}
