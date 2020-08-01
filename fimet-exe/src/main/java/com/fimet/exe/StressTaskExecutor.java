package com.fimet.exe;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import com.fimet.ExecutorManager;
import com.fimet.FimetLogger;
import com.fimet.IStoreManager;
import com.fimet.Manager;
import com.fimet.FimetLogger.Level;
import com.fimet.exe.ExecutionException;
import com.fimet.exe.IExecutable;
import com.fimet.exe.IExecutorListener;
import com.fimet.exe.IStressListener;
import com.fimet.exe.StressTask;
import com.fimet.exe.StressMultiResult.Status;
import com.fimet.exe.stress.StressIteratorFolder;
import com.fimet.exe.stress.StressIteratorSingle;
import com.fimet.stress.IStress;
import com.fimet.stress.IStressMonitor;
import com.fimet.stress.IStressStore;
import com.fimet.stress.Stress;

public class StressTaskExecutor implements IStressListener {
	static IStoreManager storeManager = Manager.get(IStoreManager.class);
	private StressTask executing;
	private IStressExecutor executor;
	private IExecutorListener listener;
	private Iterator<Stress> iterator;
	private List<IStressProcessor> processors;
	private StressMultiResult result;
	public StressTaskExecutor() {
		processors = Manager.getExtensions(IStressProcessor.class);
		executor = Manager.get(IStressExecutor.class, StressExecutor.class);
		executor.setListener(this);
	}
	public void setExecutorListener(IExecutorListener listener) {
		this.listener = listener;
	}
	public void execute(StressTask task) {
		if (FimetLogger.isEnabledDebug()) {
			FimetLogger.debug(ExecutorManager.class, "Stress test ... disabling DEBUG level");
			FimetLogger.setLevel(Level.ERROR);
		}
		result = new StressMultiResult();
		result.setStartTime(System.currentTimeMillis());
		result.setName(task.getFolder().getName());
		result.setStatus(Status.START);
		task.setResult(result);
		storeManager.bindStore(task);
		executing = task;
		executor.setStore((IStressStore)task.getStore());
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
		if (executable instanceof Stress) {
			iterator = new StressIteratorSingle((Stress)executable);
		} else if (executable instanceof File) {
			File file = (File)executable; 
			if (file.isFile()) {
				iterator = new StressIteratorSingle(file);
			} else {
				iterator = new StressIteratorFolder(file);
			}
		} else {
			clean();
			throw new ExecutionException("Invalid executable type "+executable);
		}
	}
	@Override
	public void onStressStart(IStress useCase) {
	}
	@Override
	public void onStressFinish(IStress stress) {
		result.getNumOfStressAndIncrement();
		if (processors!=null) {
			executing.incrementAndGetNumOfProcessors(processors.size());
			for (IStressProcessor processor : processors) {
				processor.process(executing, stress);
			}
		}
		checkNext();
	}
	private void checkNext() {
		if (executing!=null) {
			if (iterator.hasNext()) {
				Stress next = iterator.next();
				executor.execute(next);
			} else {
				result.setFinishTime(System.currentTimeMillis());
				result.setStatus(Status.COMPLETE);
				Task task = executing;
				clean();
				listener.onTaskFinish(task);
			}
		}
	}
	private void clean() {
		executing = null;
		iterator = null;
		result = null;
	}
	public void cancel() {
		executor.cancel();
	}
	public void setMonitor(IStressMonitor monitor) {
		executor.setMonitor(monitor);		
	}
}
