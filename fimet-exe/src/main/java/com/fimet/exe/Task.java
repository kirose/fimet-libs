package com.fimet.exe;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.LoggerFactory;import org.slf4j.Logger;

import com.fimet.IExecutorManager;
import com.fimet.Manager;
import com.fimet.Paths;
import com.fimet.stress.IStress;
import com.fimet.usecase.IUseCase;
import com.fimet.utils.Args;

public abstract class Task {
	private static Logger logger = LoggerFactory.getLogger(Task.class);
//	public static enum Type {
//		STRESS, USECASE
//	}
	public static enum State {
		QUEUED, START, ERROR, COMPLETE, STOPPED, TIMEOUT, CONNECTION_REFUSED
	}
	protected UUID id;
	protected State state;
	protected Object executable;
	protected File folder;
	protected AtomicInteger numOfProcessors = new AtomicInteger();
	protected Long startTime;
	protected Long finishTime;
	protected String message;
	
	public Task(UUID id, IStress source) {
		this(id, source, null);
	}
	public Task(UUID id, IUseCase source) {
		this(id, source, null);
	}
	public Task(Object source) {
		this(UUID.randomUUID(), source, null);
	}
	public Task(Object source, File folder) {
		this(UUID.randomUUID(), source, folder);
	}
	public Task(IExecutable exe) {
		this(UUID.randomUUID(), exe, exe.getFolder());
	}
	public Task(UUID id, Object executable, File folder) {
		Args.notNull("Task ID", id);
		Args.notNull("Task Executable", executable);
		if (folder == null) {
			folder = new File(Paths.TASKS, id.toString());
		}
		this.id = id;
		this.executable = executable;
		this.state = State.QUEUED;
		this.folder = folder;
	}
	public UUID getId() {
		return id;
	}
	public Object getExecutable() {
		return executable;
	}
	public State getState() {
		return state;
	}
	abstract public IResult getResult();
	abstract public IStore getStore();
	public void cancel() {
		Manager.getManager(IExecutorManager.class).cancel(this);
	}
	public void setState(State state) {
		this.state = state;
	}
	public File getFolder() {
		return folder;
	}
	public void finishProcessor() {
		if (decrementAndGetNumOfProcessors()==0) {
			logger.debug("Task Finished "+this);
			getStore().save();
		}
	}
	public void onResourceUpdated(File resource) {
		if (executable instanceof INotifiable) {
			((INotifiable)executable).onResourceUpdated(resource);
		}
	}
	public boolean isFinished() {
		return numOfProcessors.get() == 0;
	}
	public int getNumOfProcessors() {
		return numOfProcessors.get();
	}
	public int decrementAndGetNumOfProcessors() {
		return numOfProcessors.decrementAndGet();
	}
	public void setNumOfProcessors(int processors) {
		this.numOfProcessors.set(processors);
	}
	public int incrementAndGetNumOfProcessors(int add) {
		numOfProcessors.set(numOfProcessors.get()+add);
		return numOfProcessors.get();
	}
	public Long getStartTime() {
		return startTime;
	}
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
	public Long getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(Long finishTime) {
		this.finishTime = finishTime;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "Task " + id;
	}
}