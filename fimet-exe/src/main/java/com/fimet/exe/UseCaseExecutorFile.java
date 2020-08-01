package com.fimet.exe;

import java.io.File;

import com.fimet.ISessionManager;
import com.fimet.Manager;
import com.fimet.exe.usecase.IFinishListener;
import com.fimet.exe.usecase.NullFinishListener;
import com.fimet.usecase.IUseCaseMonitor;
import com.fimet.usecase.IUseCaseStore;
import com.fimet.usecase.UseCase;
import com.fimet.utils.UseCaseUtils;

public class UseCaseExecutorFile implements IFinishListener {
	
	static ISessionManager sessionManager = Manager.get(ISessionManager.class);
	
	private IFinishListener finishListener;
	private UseCaseExecutorUseCase executor;
	
	public UseCaseExecutorFile(IFinishListener finishListener) {
		this.finishListener = finishListener;
		this.executor = new UseCaseExecutorUseCase(this);
	}
	public void cancel() {
	}
	public void execute(UseCase useCase) {
		executor.execute(useCase);
	}
	public void execute(File useCaseFile) {
		UseCase useCase = (UseCase)UseCaseUtils.fromFile(useCaseFile);
		executor.execute(useCase);
	}
	public void setStore(IUseCaseStore store) {
		executor.setStore(store);
	}
	public void setMonitor(IUseCaseMonitor monitor) {
		executor.setMonitor(monitor);
	}
	public void onFinish(IResult result) {
		finishListener.onFinish(result);
	}
	public void setFinishListener(IFinishListener finishListener) {
		this.finishListener = finishListener!=null?finishListener:NullFinishListener.INSTANCE;
	}
	public String toString() {
		return UseCaseExecutor.class.getSimpleName();
	}
}
