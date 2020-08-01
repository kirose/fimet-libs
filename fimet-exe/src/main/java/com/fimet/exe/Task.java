package com.fimet.exe;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import com.fimet.FimetLogger;
import com.fimet.IExecutorManager;
import com.fimet.Manager;
import com.fimet.Paths;
import com.fimet.stress.IStress;
import com.fimet.usecase.IUseCase;
import com.fimet.utils.Args;

public abstract class Task {
	public static enum Type {
		STRESS, USECASE
	}
	public static enum State {
		QUEUED, START, ERROR, COMPLETE
	}
	protected UUID id;
	protected State state;
	protected Object executable;
	protected File folder;
	protected AtomicInteger numOfProcessors = new AtomicInteger();
	
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
			folder = new File(Paths.TASKS_PATH, id.toString());
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
		Manager.get(IExecutorManager.class).cancel(this);
	}
	public void setState(State state) {
		this.state = state;
	}
	public File getFolder() {
		return folder;
	}
	public void finishProcessor() {
		if (decrementAndGetNumOfProcessors()==0) {
			FimetLogger.debug(getClass(), "Task Finished "+this);
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
	@Override
	public String toString() {
		return "Task " + id;
	}
}