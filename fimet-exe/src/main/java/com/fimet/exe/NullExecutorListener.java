package com.fimet.exe;

public class NullExecutorListener implements IExecutorListener {
	public static final NullExecutorListener INSTANCE = new NullExecutorListener();
	@Override
	public void onTaskChangeStatus(Task task) {
	}
}
