package com.fimet.exe;

import java.io.File;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.fimet.AbstractManager;
import com.fimet.FimetException;
import com.fimet.FimetLogger;
import com.fimet.FimetLogger.Level;
import com.fimet.IExecutorManager;
import com.fimet.Manager;
import com.fimet.exe.IExecutorListener;
import com.fimet.exe.Task;
import com.fimet.exe.Task.State;
import com.fimet.exe.Task.Type;
import com.fimet.exe.stress.NullStressStore;
import com.fimet.exe.usecase.NullUseCaseStore;
import com.fimet.stress.IStress;
import com.fimet.stress.IStressExecutor;
import com.fimet.stress.IStressExecutorListener;
import com.fimet.stress.IStressStore;
import com.fimet.usecase.IUseCase;
import com.fimet.usecase.IUseCaseExecutor;
import com.fimet.usecase.IUseCaseExecutorListener;
import com.fimet.usecase.IUseCaseStore;

public class ExecutorManager extends AbstractManager implements IExecutorManager, IExecutorListener {
	private IUseCaseExecutor useCaseExecutor;
	private IStressExecutor stressExecutor;
	private ConcurrentLinkedQueue<Task> queue;
	private IExecutorListener listener;
	volatile private Task executing;
	public ExecutorManager() {
		listener = NullExecutorListener.INSTANCE;
		queue = new ConcurrentLinkedQueue<Task>();
	}
	@Override
	public void start() {
		IUseCaseStore useCaseStore = Manager.getExtension(IUseCaseStore.class, NullUseCaseStore.class);
		useCaseExecutor = Manager.getExtension(IUseCaseExecutor.class, UseCaseExecutor.class);
		useCaseExecutor.setStore(useCaseStore);
		useCaseExecutor.setExecutorListener(this);
		
		IStressStore stressStore = Manager.getExtension(IStressStore.class, NullStressStore.class);
		stressExecutor = Manager.getExtension(IStressExecutor.class, StressExecutor.class);
		stressExecutor.setStore(stressStore);
		stressExecutor.setExecutorListener(this);
	}
	private void checkQueue() {
		if (executing == null && !queue.isEmpty()) {
			executing = queue.poll();
			executing.state = State.START;
			switch (executing.type) {
				case USECASE:
					useCaseExecutor.execute(executing);
					break;
				case STRESS:
					if (FimetLogger.isEnabledDebug()) {
						FimetLogger.debug(ExecutorManager.class, "Stress test ... disabling DEBUG level");
						FimetLogger.setLevel(Level.ERROR);
					}
					stressExecutor.execute(executing);
					break;
				default:
					throw new FimetException("Invalid type "+executing.type);
			}
		}
	}
	@Override
	public void onTaskFinish(Task task) {
		executing = null;
		task.state = State.COMPLETE;
		listener.onTaskFinish(task);
		checkQueue();
	}
	@Override
	public Task executeUseCase(Object useCase) {
		return execute(new Task(Type.USECASE, useCase));
	}
	@Override
	public Task execute(IUseCase useCase) {
		return execute(new Task(Type.USECASE, useCase));
	}
	@Override
	public Task execute(File fileOrFolder) {
		return execute(new Task(Type.USECASE, fileOrFolder));
	}
	@Override
	public Task execute(String pathFileOrFolder) {
		return execute(new Task(Type.USECASE, new File(pathFileOrFolder)));
	}
	@Override
	public Task executeStress(Object stress) {
		return execute(new Task(Type.STRESS, stress));
	}
	@Override
	public Task execute(IStress stress) {
		return execute(new Task(Type.STRESS, stress));
	}
	@Override
	public Task execute(Task task) {
		queue.add(task);
		checkQueue();
		return task;
	}
	@Override
	public void cancel(Task task) {
		if (task == executing) {
			switch (executing.type) {
			case USECASE:
				useCaseExecutor.cancel();
				break;
			case STRESS:
				stressExecutor.cancel();
			}
		} else {
			queue.remove(task);
		}
	}
	@Override
	public void setExecutorListener(IExecutorListener listener) {
		this.listener = listener != null ? listener : NullExecutorListener.INSTANCE; 
	}
	@Override
	public void setUseCaseExecutorListener(IUseCaseExecutorListener listener) {
		useCaseExecutor.setListener(listener);
	}
	@Override
	public void setStressExecutorListener(IStressExecutorListener listener) {
		stressExecutor.setListener(listener);
	}
	@Override
	public void reload() {}
	@Override
	public void saveState() {}
	@Override
	public Task getRunningTask() {
		return executing;
	}
}

