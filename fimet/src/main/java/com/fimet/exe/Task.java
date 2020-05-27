package com.fimet.exe;

import java.io.File;
import java.util.UUID;

import com.fimet.IExecutorManager;
import com.fimet.Manager;
import com.fimet.stress.IStress;
import com.fimet.usecase.IUseCase;

public class Task {
	public static final File STORE_PATH = new File("store");
	public static final File TASKS_PATH = new File(STORE_PATH,"tasks");
	static {
		if (!STORE_PATH.exists()) {
			STORE_PATH.mkdirs();
		}
	}
	public static enum Type {
		STRESS, USECASE
	}
	public static enum State {
		QUEUED, START, ERROR, COMPLETE
	}

	UUID id;
	Type type;
	State state;
	Object source;
	Object result;

	public Task(UUID id, IStress source) {
		this(id, Type.STRESS, source);
	}
	public Task(UUID id, IUseCase source) {
		this(id, Type.USECASE, source);
	}
	Task(Type type, Object source) {
		this(null, type, source);
	}
	Task(UUID id, Type type, Object source) {
		this.id = id!=null?id:UUID.randomUUID();
		this.type = type;
		this.source = source;
		this.state = State.QUEUED;
	}
	public UUID getId() {
		return id;
	}
	public Type getType() {
		return type;
	}
	public Object getSource() {
		return source;
	}
	public State getState() {
		return state;
	}
	public Object getResult() {
		return result;
	}
	public void cancel() {
		Manager.get(IExecutorManager.class).cancel(this);
	}
	@Override
	public String toString() {
		return "Task " + id;
	}
}