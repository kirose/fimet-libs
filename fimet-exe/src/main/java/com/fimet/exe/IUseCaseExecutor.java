package com.fimet.exe;


import com.fimet.IManager;
import com.fimet.usecase.IUseCaseMonitor;
import com.fimet.usecase.IUseCaseStore;
import com.fimet.usecase.UseCase;

public interface IUseCaseExecutor extends IManager {
	void execute(UseCase useCase);
	void cancel();
	void setMonitor(IUseCaseMonitor monitor);
	void setListener(IUseCaseListener listener);
	void setStore(IUseCaseStore store);
}
