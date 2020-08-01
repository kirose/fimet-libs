package com.fimet.exe;

import java.io.File;

import com.fimet.FimetLogger;
import com.fimet.exe.UseCaseResult.Status;
import com.fimet.exe.usecase.UseCaseIteratorFolder;
import com.fimet.exe.usecase.IFinishListener;
import com.fimet.exe.usecase.NullFinishListener;
import com.fimet.usecase.IUseCaseMonitor;
import com.fimet.usecase.IUseCaseStore;

public class UseCaseExecutorFolder implements IFinishListener {
	private UseCaseIteratorFolder iterator;
	private IFinishListener finishListener;
	private UseCaseMultiResult result;
	private UseCaseExecutorUseCase executor;
	public UseCaseExecutorFolder(IFinishListener finishListener) {
		this.executor = new UseCaseExecutorUseCase(this);
		this.finishListener = finishListener;
	}
	public void cancel() {
		if (iterator != null) {
			iterator.close();
			iterator = null;
		}
	}
	public void execute(File folder) {
		try {
			this.result = new UseCaseMultiResult();
			result.setName(folder.getName());
			result.setStatus(UseCaseMultiResult.Status.START);
			this.result.setStartTime(System.currentTimeMillis());
			this.iterator = new UseCaseIteratorFolder(folder);
			if(iterator.hasNext()) {
				executor.execute(iterator.next());
			} else {
				result.setStatus(UseCaseMultiResult.Status.COMPLETE);
				finishListener.onFinish(result);
			}
		} catch(Exception e) {
			FimetLogger.error(UseCaseExecutor.class, e);
			result.setStatus(UseCaseMultiResult.Status.ERROR);
			finishListener.onFinish(result);
		}
	}
	@Override
	public void onFinish(IResult result) {
		if (iterator != null) {
			this.result.getNumOfUseCasesAndIncrement();
			Status status = ((UseCaseResult)result).getStatus();
			if (status == Status.COMPLETE) {
				this.result.getNumOfCompleteAndIncrement();
			} else if (status == Status.ERROR || status == Status.CONNECTION_REFUSED) {
				this.result.getNumOfErrorAndIncrement();
			} else if (status == Status.TIMEOUT) {
				this.result.getNumOfTimeoutAndIncrement();
			}
			if (iterator.hasNext()) {
				executor.execute(iterator.next());
			} else {
				this.result.setFinishTime(System.currentTimeMillis());
				finishListener.onFinish(this.result);
				iterator = null;
				this.result = null;
				this.iterator = null;
			}
		}
	}
	public void setStore(IUseCaseStore store) {
		executor.setStore(store);
	}
	public void setMonitor(IUseCaseMonitor monitor) {
		executor.setMonitor(monitor);
	}
	public void setFinishListener(IFinishListener finishListener) {
		this.finishListener = finishListener!=null?finishListener:NullFinishListener.INSTANCE;
	}
}
