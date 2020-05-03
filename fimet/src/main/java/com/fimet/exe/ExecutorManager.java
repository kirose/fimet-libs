package com.fimet.exe;

import java.io.File;

import com.fimet.IExecutorManager;
import com.fimet.Manager;
import com.fimet.stress.Stress;
import com.fimet.stress.exe.StressExecutor;
import com.fimet.stress.exe.StressStore;
import com.fimet.usecase.UseCase;
import com.fimet.usecase.exe.UseCaseExecutor;
import com.fimet.usecase.exe.UseCaseStore;

public class ExecutorManager implements IExecutorManager {
	
	private IUseCaseExecutor useCaseExecutor;
	private IStressExecutor stressExecutor;
	public ExecutorManager() {
		IUseCaseStore useCaseStore = Manager.newInstanceForExtension(IUseCaseStore.class, UseCaseStore.class);
		useCaseExecutor = Manager.newInstanceForExtension(IUseCaseExecutor.class, UseCaseExecutor.class);
		useCaseExecutor.setStore(useCaseStore);
		useCaseExecutor.startExecutor();
		
		IStressStore stressStore = Manager.newInstanceForExtension(IStressStore.class, StressStore.class);
		stressExecutor = Manager.newInstanceForExtension(IStressExecutor.class, StressExecutor.class);
		stressExecutor.setStore(stressStore);
		stressExecutor.startExecutor();
	}
	@Override
	public void free() {
	}
	@Override
	public void saveState() {
	}
	@Override
	public void execute(UseCase useCase, IUseCaseExecutorListener listener) {
		useCaseExecutor.setListener(listener);
		useCaseExecutor.execute(useCase);
	}
	@Override
	public void execute(UseCase useCase) {
		useCaseExecutor.execute(useCase);
	}
	@Override
	public void execute(File fileOrFolder) {
		useCaseExecutor.execute(fileOrFolder);
	}
	@Override
	public void execute(File fileOrFolder, IUseCaseExecutorListener listener) {
		useCaseExecutor.setListener(listener);
		useCaseExecutor.execute(fileOrFolder);
	}
	@Override
	public void execute(String pathFileOrFolder) {
		execute(new File(pathFileOrFolder));
	}
	@Override
	public void execute(String pathFileOrFolder, IUseCaseExecutorListener listener) {
		execute(new File(pathFileOrFolder), listener);
	}
	@Override
	public void execute(Stress stress, IStressExecutorListener listener) {
		stressExecutor.setListener(listener);
		stressExecutor.execute(stress);
	}
	@Override
	public void execute(Stress stress) {
		stressExecutor.execute(stress);
	}
	@Override
	public IStressExecutor getStressExecutor() {
		return stressExecutor;
	}
	@Override
	public IUseCaseExecutor getUseCaseExecutor() {
		return useCaseExecutor;
	}
}
