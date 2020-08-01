package com.fimet;

import com.fimet.exe.IStore;
import com.fimet.exe.StressTask;
import com.fimet.exe.Task;
import com.fimet.exe.UseCaseTask;
import com.fimet.stress.StressStore;
import com.fimet.usecase.UseCaseStore;

public class StoreManager implements IStoreManager {

	@Override
	public void start() {
	}

	@Override
	public void reload() {
	}

	@Override
	public IStore bindStore(Task task) {
		if (task instanceof UseCaseTask) {
			((UseCaseTask)task).setStore(new UseCaseStore());
		} else if (task instanceof StressTask) {
			((StressTask)task).setStore(new StressStore());
		} else {
			throw new FimetException("Invalid task type "+task.getClass());
		}
		task.getStore().init(task);
		return task.getStore();
	}

}
