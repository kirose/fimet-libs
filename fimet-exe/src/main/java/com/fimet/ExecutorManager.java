package com.fimet;

import java.io.File;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.fimet.AbstractManager;
import com.fimet.FimetException;
import com.fimet.FimetLogger;
import com.fimet.IExecutorManager;
import com.fimet.exe.IExecutable;
import com.fimet.exe.IExecutorListener;
import com.fimet.exe.INotifiable;
import com.fimet.exe.StressTask;
import com.fimet.exe.StressTaskExecutor;
import com.fimet.exe.Task;
import com.fimet.exe.UseCaseTask;
import com.fimet.exe.UseCaseTaskExecutor;
import com.fimet.exe.Task.State;
import com.fimet.stress.IStress;
import com.fimet.stress.IStressMonitor;
import com.fimet.usecase.IUseCase;
import com.fimet.usecase.IUseCaseMonitor;

public class ExecutorManager extends AbstractManager implements IExecutorManager, IExecutorListener {
	private UseCaseTaskExecutor useCaseExecutor;
	private StressTaskExecutor stressExecutor;
	private ConcurrentLinkedQueue<Task> queue;
	private Queue<IExecutorListener> listeners;
	volatile private Task executing;
	public ExecutorManager() {
		listeners = new ConcurrentLinkedQueue<IExecutorListener>();
		queue = new ConcurrentLinkedQueue<Task>();
	}
	@Override
	public void start() {
		useCaseExecutor = new UseCaseTaskExecutor();
		useCaseExecutor.setExecutorListener(this);
		stressExecutor = new StressTaskExecutor();
		stressExecutor.setExecutorListener(this);
	}
	private void checkQueue() {
		if (executing == null && !queue.isEmpty()) {
			executing = queue.poll();
			if (executing instanceof UseCaseTask) {
				useCaseExecutor.execute((UseCaseTask)executing);
			} else if (executing instanceof StressTask) {
				stressExecutor.execute((StressTask)executing);
			} else {
					throw new FimetException("Invalid type "+executing);
			}
		}
	}
	@Override
	public void onTaskStart(Task task) {
		executing.setState(State.START);
	}
	@Override
	public void onTaskFinish(Task task) {
		executing = null;
		task.setState(State.COMPLETE);
		if (!listeners.isEmpty()) {
			try {
				for (IExecutorListener listener : listeners) {
					listener.onTaskFinish(task);
				}
			} catch (Throwable e) {
				FimetLogger.error(getClass(), e);
			}
		}
		if (task.getExecutable() instanceof INotifiable) {
			((INotifiable)task.getExecutable()).onExecutionFinish(task);
		}
		task.getStore().close();
		checkQueue();
	}
	@Override
	public Task execute(Task task) {
		queue.add(task);
		checkQueue();
		return task;
	}
	@Override
	public Task executeUseCase(IExecutable executable) {
		return execute(new UseCaseTask(executable));
	}
	@Override
	public Task executeUseCase(Object useCase) {
		return execute(new UseCaseTask(useCase));
	}
	@Override
	public Task executeUseCase(Object useCase, File folder) {
		return execute(new UseCaseTask(useCase, folder));
	}
	@Override
	public Task execute(IUseCase useCase) {
		return execute(new UseCaseTask(useCase));
	}
	@Override
	public Task execute(IUseCase useCase, File folder) {
		return execute(new UseCaseTask(useCase, folder));
	}
	@Override
	public Task executeUseCase(File fileOrFolder) {
		return execute(new UseCaseTask(fileOrFolder));
	}
	@Override
	public Task executeUseCase(File fileOrFolder, File folder) {
		return execute(new UseCaseTask(fileOrFolder, folder));
	}
	@Override
	public Task executeUseCase(String pathFileOrFolder) {
		return execute(new UseCaseTask(new File(pathFileOrFolder)));
	}
	@Override
	public Task executeUseCase(String pathFileOrFolder, File folder) {
		return execute(new UseCaseTask(new File(pathFileOrFolder), folder));
	}
	@Override
	public Task executeStress(IExecutable executable) {
		return execute(new UseCaseTask(executable));
	}
	@Override
	public Task executeStress(Object stress) {
		return execute(new StressTask(stress));
	}
	@Override
	public Task executeStress(Object stress, File folder) {
		return execute(new StressTask(stress, folder));
	}
	@Override
	public Task execute(IStress stress) {
		return execute(new StressTask(stress));
	}
	@Override
	public Task execute(IStress stress, File folder) {
		return execute(new StressTask(stress, folder));
	}
	@Override
	public void cancel(Task task) {
		if (task == executing) {
			if (executing instanceof UseCaseTask) {
				useCaseExecutor.cancel();
			} else if (executing instanceof StressTask) {
				stressExecutor.cancel();
			}
		} else {
			queue.remove(task);
		}
	}
	@Override
	public void addExecutorListener(IExecutorListener listener) {
		if (listener != null) {
			this.listeners.add(listener);
		}
	}
	@Override
	public void removeExecutorListener(IExecutorListener listener) {
		if(listener!=null) {
			this.listeners.remove(listener);
		}
	}
	@Override
	public void reload() {}
	@Override
	public Task getRunningTask() {
		return executing;
	}
	@Override
	public void setUseCaseMonitor(IUseCaseMonitor monitor) {
		useCaseExecutor.setMonitor(monitor);
	}
	@Override
	public void setStressMonitor(IStressMonitor monitor) {
		stressExecutor.setMonitor(monitor);
	}
}

