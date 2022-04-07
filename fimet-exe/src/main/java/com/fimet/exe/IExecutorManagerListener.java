package com.fimet.exe;

public interface IExecutorManagerListener {
	void onTaskError(Task task);
	void onTaskStart(Task task);
	void onTaskFinish(Task task);
}
