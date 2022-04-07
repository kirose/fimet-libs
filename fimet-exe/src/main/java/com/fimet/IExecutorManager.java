package com.fimet;

import java.io.File;

import com.fimet.exe.ExecutionException;
import com.fimet.exe.IExecutable;
import com.fimet.exe.IExecutorListener;
import com.fimet.exe.Task;
import com.fimet.exe.Task.State;
import com.fimet.stress.IStress;
import com.fimet.stress.IStressMonitor;
import com.fimet.usecase.IUseCase;
import com.fimet.usecase.IUseCaseMonitor;
/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface IExecutorManager extends IManager {
	
	Task executeUseCase(IExecutable executable);
	Task execute(Task task) throws ExecutionException;
	Task executeUseCase(Object useCase) throws ExecutionException;
	Task executeUseCase(Object useCase, File folder) throws ExecutionException;
	Task execute(IUseCase useCase) throws ExecutionException;
	Task execute(IUseCase useCase, File folder) throws ExecutionException;
	Task executeUseCase(File fileOrFolderUseCase) throws ExecutionException;
	Task executeUseCase(File fileOrFolderUseCase, File folder) throws ExecutionException;
	Task executeUseCase(String pathFileOrFolderUseCase) throws ExecutionException;
	Task executeUseCase(String pathFileOrFolderUseCase, File folder) throws ExecutionException;
	Task executeStress(IExecutable executable);
	Task executeStress(Object stress) throws ExecutionException;
	Task executeStress(Object stress, File folder) throws ExecutionException;
	Task execute(IStress stress) throws ExecutionException;
	Task execute(IStress stress, File folder) throws ExecutionException;

	void addExecutorListener(IExecutorListener listener);
	void removeExecutorListener(IExecutorListener listener);
	void setUseCaseMonitor(IUseCaseMonitor monitor);
	void setStressMonitor(IStressMonitor monitor);
	void cancel(Task task);
	Task getRunningTask();
	State getState(String idTask);
	void stop(String idTask);
}
