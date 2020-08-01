package com.fimet.exe;

import com.fimet.IManager;
import com.fimet.stress.IStressMonitor;
import com.fimet.stress.IStressStore;
import com.fimet.stress.Stress;

public interface IStressExecutor extends IManager {
	void execute(Stress stress);
	void setMonitor(IStressMonitor monitor);
	void setListener(IStressListener executorManager);
	void setStore(IStressStore store);
	void cancel();
}
