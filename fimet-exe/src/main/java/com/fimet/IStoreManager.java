package com.fimet;

import com.fimet.exe.IStore;
import com.fimet.exe.Task;

public interface IStoreManager extends IManager {
	IStore bindStore(Task task);
}
