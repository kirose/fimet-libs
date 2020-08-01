package com.fimet.exe;

public interface IStore {
	void init(Task task, Object ...params);
	void close();
	void save();
}
