package com.fimet.exe;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.LoggerFactory;import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import com.fimet.IStoreManager;
import com.fimet.Manager;
import com.fimet.exe.ExecutionException;
import com.fimet.exe.IExecutable;
import com.fimet.exe.IUseCaseListener;
import com.fimet.exe.Task;
import com.fimet.exe.UseCaseMultiResult;
import com.fimet.exe.UseCaseResult.State;
import com.fimet.exe.UseCaseTask;
import com.fimet.exe.usecase.UseCaseIteratorMulti;
import com.fimet.exe.usecase.UseCaseIteratorMultiFile;
import com.fimet.exe.usecase.UseCaseIteratorSingle;
import com.fimet.usecase.IUseCase;
import com.fimet.usecase.IUseCaseMonitor;
import com.fimet.usecase.IUseCaseStore;
import com.fimet.usecase.UseCase;

@Component
public class UseCaseTaskExecutor implements IUseCaseListener {
	private static Logger logger = LoggerFactory.getLogger(UseCaseTaskExecutor.class);
	@Autowired private IStoreManager storeManager;
	@Autowired private Manager manager;
	@Autowired private IUseCaseExecutor executor;
	private IExecutorManagerListener listener;
	private List<IUseCaseProcessor> processors;
	private UseCaseTask executing;
	private Iterator<UseCase> iterator;
	private UseCaseMultiResult result;
	public UseCaseTaskExecutor() {}
	@PostConstruct
	public void start() {
		processors = manager.getExtensions(IUseCaseProcessor.class);
		logger.debug("NumOfProcessors:"+(processors!=null?processors.size():0));
		executor.setListener(this);
	}
	public void setExecutorListener(IExecutorManagerListener listener) {
		this.listener = listener;
	}
	public void execute(UseCaseTask task) {
		storeManager.bindStore(task);
		executor.setStore((IUseCaseStore)task.getStore());
		executing = task;
		result = new UseCaseMultiResult(task);
		result.setName(task.getFolder().getName());
		task.setResult(result);
		listener.onTaskStart(executing);
		prepareIterator();
		checkNext();
	}
	@SuppressWarnings("unchecked")
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
			iterator = new UseCaseIteratorMultiFile((File)executable);
		} else if (executable instanceof File[]) {
			iterator = new UseCaseIteratorMultiFile((File[])executable);
		} else if (executable instanceof UseCase[]) {
			iterator = new UseCaseIteratorMulti(Arrays.asList((UseCase[])executable).iterator());
		} else if (executable instanceof List) {
			List<?> list = (List<?>)executable;
			if (list.get(0) instanceof File) {
				iterator = new UseCaseIteratorMultiFile((List<File>)executable);
			} else if (list.get(0) instanceof UseCase) {
				iterator = new UseCaseIteratorMulti(((List<UseCase>)executable).iterator());
			} else {
				clean();
				throw new ExecutionException("Invalid executable type "+executable);
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
			State state = useCase.getResult().getState();
			if (state == State.COMPLETE) {
				result.getNumOfCompleteAndIncrement();
			} else if (state == State.ERROR || state == State.CONNECTION_REFUSED) {
				result.getNumOfErrorAndIncrement();
			} else if (state == State.TIMEOUT) {
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
