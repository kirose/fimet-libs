package com.fimet;

import java.io.File;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.annotation.PostConstruct;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fimet.AbstractManager;
import com.fimet.FimetException;

import com.fimet.IExecutorManager;
import com.fimet.exe.IExecutable;
import com.fimet.exe.IExecutorListener;
import com.fimet.exe.IExecutorManagerListener;
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

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.Level;

@Component
public class ExecutorManager extends AbstractManager implements IExecutorManager, IExecutorManagerListener, Runnable {
	
	private static Logger logger = (Logger)LoggerFactory.getLogger(ExecutorManager.class);
	
	@Autowired private UseCaseTaskExecutor useCaseExecutor;
	@Autowired private StressTaskExecutor stressExecutor;
	@Autowired private IThreadManager threadManager;
	
	private ConcurrentLinkedQueue<Task> queue;
	private Queue<IExecutorListener> listeners;
	volatile private Task executing;
	private Level level;
	
	public ExecutorManager() {
		listeners = new ConcurrentLinkedQueue<IExecutorListener>();
		queue = new ConcurrentLinkedQueue<Task>();
	}
	@PostConstruct
	@Override
	public void start() {
		level = logger.getLevel();
		useCaseExecutor.setExecutorListener(this);
		stressExecutor.setExecutorListener(this);
	}
	private void checkQueue() {
		if (executing == null && !queue.isEmpty()) {
			executing = queue.poll();
			threadManager.execute(this);
		}
	}
	public void run() {
		try {
			if (executing instanceof UseCaseTask) {
				useCaseExecutor.execute((UseCaseTask)executing);
			} else if (executing instanceof StressTask) {
				if (logger.isDebugEnabled()) {
					level = logger.getLevel();
					logger.debug("Stress test ... disabling DEBUG level");
					logger.setLevel(Level.ERROR);
				}
				stressExecutor.execute((StressTask)executing);
			} else {
				throw new FimetException("Invalid type "+executing);
			}
		} catch (Exception e) {
			logger.error("Execution error "+executing,e);
			onTaskError(executing);
		}		
	}
	@Override
	public void onTaskStart(Task task) {
		task.setStartTime(System.currentTimeMillis());
		task.setState(State.START);
		doNotifyTaskChangeStatus(task);
	}
	@Override
	public void onTaskFinish(Task task) {
		task.setFinishTime(System.currentTimeMillis());
		task.setState(State.COMPLETE);
		doTaskFinish(task);
	}
	@Override
	public void onTaskError(Task task) {
		task.setFinishTime(System.currentTimeMillis());
		task.setState(State.ERROR);
		doTaskFinish(task);
	}
	public void doTaskFinish(Task task) {
		if (task instanceof StressTask && level != logger.getLevel()) {
			logger.setLevel(level);
		}
		executing = null;
		doNotifyTaskChangeStatus(task);
		if (task.getExecutable() instanceof INotifiable) {
			try {
				((INotifiable)task.getExecutable()).onExecutionFinish(task);
			} catch (Exception e) {
				logger.error("Task Notification Exception", e);
			}
		}
		task.getStore().close();
		checkQueue();
	}
	private void doNotifyTaskChangeStatus(Task task) {
		threadManager.execute(()->{
			if (!listeners.isEmpty()) {
				try {
					for (IExecutorListener listener : listeners) {
						listener.onTaskChangeStatus(task);
					}
				} catch (Throwable e) {
					logger.error("Listener notification exception", e);
				}
			}
		});
	}
	@Override
	public Task execute(Task task) {
		queue.add(task);
		task.setStartTime(System.currentTimeMillis());
		task.setState(State.QUEUED);
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
		return execute(new UseCaseTask(new File(pathFileOrFolder).getAbsoluteFile()));
	}
	@Override
	public Task executeUseCase(String pathFileOrFolder, File folder) {
		return execute(new UseCaseTask(new File(pathFileOrFolder).getAbsoluteFile(), folder));
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
	public State getState(String idTask) {
		if (executing!=null && executing.getId().toString().equals(idTask)) {
			return executing.getState();
		}
		if (!queue.isEmpty()) {
			for (Task t : queue) {
				if (t.getId().toString().equals(idTask)) {
					return t.getState();
				}
			}
		}
		return Task.State.COMPLETE;
	}
	public void stop(String idTask) {
		if (executing!=null && executing.getId().toString().equals(idTask)) {
			executing.cancel();
		}
		if (!queue.isEmpty()) {
			for (Task t : queue) {
				if (t.getId().toString().equals(idTask)) {
					t.cancel();
				}
			}
		}
	}
	@Override
	public void stop() {}
}

