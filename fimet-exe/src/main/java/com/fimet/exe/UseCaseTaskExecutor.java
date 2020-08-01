package com.fimet.exe;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import com.fimet.FimetLogger;
import com.fimet.IStoreManager;
import com.fimet.Manager;
import com.fimet.exe.ExecutionException;
import com.fimet.exe.IExecutable;
import com.fimet.exe.IExecutorListener;
import com.fimet.exe.IUseCaseListener;
import com.fimet.exe.Task;
import com.fimet.exe.UseCaseMultiResult;
import com.fimet.exe.UseCaseTask;
import com.fimet.exe.UseCaseResult.Status;
import com.fimet.exe.usecase.UseCaseIteratorFolder;
import com.fimet.exe.usecase.UseCaseIteratorSingle;
import com.fimet.usecase.IUseCase;
import com.fimet.usecase.IUseCaseMonitor;
import com.fimet.usecase.IUseCaseStore;
import com.fimet.usecase.UseCase;

public class UseCaseTaskExecutor implements IUseCaseListener {
	static IStoreManager storeManager = Manager.get(IStoreManager.class);
	private IUseCaseExecutor executor;
	private IExecutorListener listener;
	private List<IUseCaseProcessor> processors;
	private UseCaseTask executing;
	private Iterator<UseCase> iterator;
	private UseCaseMultiResult result;
	public UseCaseTaskExecutor() {
		processors = Manager.getExtensions(IUseCaseProcessor.class);
		FimetLogger.debug(getClass(), "NumOfProcessors:"+(processors!=null?processors.size():0));
		executor = Manager.get(IUseCaseExecutor.class, UseCaseExecutor.class);
		executor.setListener(this);
	}
	public void setExecutorListener(IExecutorListener listener) {
		this.listener = listener;
	}
	public void execute(UseCaseTask task) {
		storeManager.bindStore(task);
		executor.setStore((IUseCaseStore)task.getStore());
		executing = task;
		result = new UseCaseMultiResult();
		result.setName(task.getFolder().getName());
		result.setStatus(UseCaseMultiResult.Status.START);
		result.setStartTime(System.currentTimeMillis());
		task.setResult(result);
		prepareIterator();
		checkNext();
	}
	private void prepareIterator() {
		Object executable;
		if (executing.getExecutable() instanceof IExecutable) {
			executable = ((IExecutable)executing.getExecutable()).getExecutable();
		} else {
			executable = executing.getExecutable();
		}
		if (executable instanceof UseCase) {
			iterator = new UseCaseIteratorSingle((UseCase)executable);
		} else if (executable instanceof File) {
			File file = (File)executable; 
			if (file.isFile()) {
				iterator = new UseCaseIteratorSingle(file);
			} else {
				iterator = new UseCaseIteratorFolder(file);
			}
		} else {
			clean();
			throw new ExecutionException("Invalid executable type "+executable);
		}
	}
	@Override
	public void onUseCaseStart(IUseCase useCase) {
	}
	@Override
	public void onUseCaseFinish(IUseCase useCase) {
		if (executing!=null) {
			Status status = useCase.getResult().getStatus();
			if (status == Status.COMPLETE) {
				result.getNumOfCompleteAndIncrement();
			} else if (status == Status.ERROR || status == Status.CONNECTION_REFUSED) {
				result.getNumOfErrorAndIncrement();
			} else if (status == Status.TIMEOUT) {
				result.getNumOfTimeoutAndIncrement();
			}
		}
		if (processors!=null) {
			executing.incrementAndGetNumOfProcessors(processors.size());
			for (IUseCaseProcessor processor : processors) {
				processor.process(executing, useCase);
			}
		}
		checkNext();
	}
	private void checkNext() {
		if (executing!=null) {
			if (iterator.hasNext()) {
				UseCase next = iterator.next();
				executor.execute(next);
			} else {
				result.setFinishTime(System.currentTimeMillis());
				result.setStatus(UseCaseMultiResult.Status.COMPLETE);
				Task task = executing;
				clean();
				listener.onTaskFinish(task);
			}
		}
	}
	private void clean() {
		executor.setStore(null);
		executing = null;
		result = null;
		iterator = null;
	}
	public void cancel() {
		executor.cancel();
	}
	public void setMonitor(IUseCaseMonitor monitor) {
		executor.setMonitor(monitor);
	}
}
