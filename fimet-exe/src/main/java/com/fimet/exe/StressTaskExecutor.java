package com.fimet.exe;

import java.io.File;
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
import com.fimet.exe.IStressListener;
import com.fimet.exe.StressTask;
import com.fimet.exe.stress.StressIteratorFolder;
import com.fimet.exe.stress.StressIteratorSingle;
import com.fimet.stress.IStress;
import com.fimet.stress.IStressMonitor;
import com.fimet.stress.IStressStore;
import com.fimet.stress.Stress;

@Component
public class StressTaskExecutor implements IStressListener {
	private static Logger logger = LoggerFactory.getLogger(StressTaskExecutor.class);
	@Autowired private IStoreManager storeManager;
	@Autowired private Manager manager;
	@Autowired private IStressExecutor executor;
	private StressTask executing;
	private IExecutorManagerListener listener;
	private Iterator<Stress> iterator;
	private List<IStressProcessor> processors;
	private StressMultiResult result;
	public StressTaskExecutor() {
	}
	@PostConstruct
	private void start() {
		processors = manager.getExtensions(IStressProcessor.class);
		executor.setListener(this);
	}
	public void setExecutorListener(IExecutorManagerListener listener) {
		this.listener = listener;
	}
	public void execute(StressTask task) {
		result = new StressMultiResult(task);
		result.setName(task.getFolder().getName());
		task.setResult(result);
		storeManager.bindStore(task);
		executing = task;
		executor.setStore((IStressStore)task.getStore());
		listener.onTaskStart(executing);
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
			iterator = new StressIteratorSingle((Stress)executable, executing.getFolder());
		} else if (executable instanceof File) {
			File file = (File)executable; 
			if (file.isFile()) {
				iterator = new StressIteratorSingle(file, executing.getFolder());
			} else {
				iterator = new StressIteratorFolder(file, executing.getFolder());
			}
		} else {
			clean();
			throw new ExecutionException("Invalid executable type "+executable);
		}
	}
	@Override
	public void onStressStart(IStress stress) {
	}
	@Override
	public void onStressError(IStress stress, Exception e) {
		logger.error("Task Stress Exception",e);
		executing.setMessage(e.getMessage());
		Task task = executing;
		clean();
		listener.onTaskError(task);
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
				executing.setFinishTime(System.currentTimeMillis());
				executing.setState(Task.State.COMPLETE);
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
