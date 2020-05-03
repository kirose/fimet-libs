package com.fimet.exe;

import com.fimet.stress.Stress;

public interface IStressExecutor {
	void startExecutor();
	void stopExecutor();
	void execute(Stress stress);
	void setListener(IStressExecutorListener listener);
	void setStore(IStressStore store);
}
