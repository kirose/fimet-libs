package com.fimet.usecase;

import java.io.File;

import com.fimet.IUseCaseManager;
import com.fimet.Manager;
import com.fimet.usecase.exe.IExecutorListener;
import com.fimet.usecase.exe.IStoreResults;
import com.fimet.usecase.exe.LogFilesStore;
import com.fimet.usecase.exe.SyncExecutor;

public class UseCaseManager implements IUseCaseManager {
	
	private SyncExecutor syncExecutor;
//	private IExecutor asyncExecutor;
	public UseCaseManager() {
		IStoreResults store = Manager.newInstanceForExtension(IStoreResults.class, LogFilesStore.class);
		syncExecutor = new SyncExecutor();
		syncExecutor.setStore(store);
		syncExecutor.startExecutor();
//		asyncExecutor = null;
	}
	@Override
	public void free() {
	}
	@Override
	public void saveState() {
	}
	@Override
	public void execute(UseCase useCase, IExecutorListener listener) {
		syncExecutor.setListener(listener);
		syncExecutor.execute(useCase);
	}
	@Override
	public void execute(UseCase useCase) {
		syncExecutor.execute(useCase);
	}
	@Override
	public void execute(File fileOrFolder) {
		syncExecutor.execute(fileOrFolder);
	}
	@Override
	public void setExcutorListener(IExecutorListener listener) {
		syncExecutor.setListener(listener);
	}
	@Override
	public void execute(File fileOrFolder, IExecutorListener listener) {
		syncExecutor.setListener(listener);
		syncExecutor.execute(fileOrFolder);
	}
	@Override
	public void execute(String pathFileOrFolder) {
		execute(new File(pathFileOrFolder));
	}
	@Override
	public void execute(String pathFileOrFolder, IExecutorListener listener) {
		execute(new File(pathFileOrFolder), listener);
	}

}
