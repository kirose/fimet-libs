package com.fimet.exe;

public interface IExecutorListener {
	void onTaskStart(Task task);
	void onTaskFinish(Task task);
}
