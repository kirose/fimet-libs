package com.fimet.exe;

import java.io.File;
import java.util.UUID;

import com.fimet.usecase.IRUseCase;
import com.fimet.usecase.IUseCase;
import com.fimet.usecase.IUseCaseStore;

public class UseCaseTask extends Task {
	private UseCaseMultiResult result;
	private IUseCaseStore store;
	public UseCaseTask(Object source) {
		super(UUID.randomUUID(), source, null);
	}
	public UseCaseTask(Object source, File folder) {
		super(UUID.randomUUID(), source, folder);
	}
	public UseCaseTask(UUID id, Object source) {
		super(id, source, null);
	}
	public UseCaseTask(UUID id, Object source, File folder) {
		super(id, source, folder);
	}
	public UseCaseTask(IExecutable exe) {
		super(UUID.randomUUID(), exe, exe.getFolder());
	}
	public void setResult(UseCaseMultiResult result) {
		this.result = result;
	}
	public void setStore(IUseCaseStore store) {
		this.store = store;
	}
	@Override
	public UseCaseMultiResult getResult() {
		return result;
	}
	@Override
	public IUseCaseStore getStore() {
		return store;
	}
	public void onEntityUpdated(IRUseCase entity) {
		if (executable instanceof INotifiable) {
			((INotifiable)executable).onEntityUpdated(entity);
		}
	}
	public void storeUseCase(IUseCase useCase) {
		store.storeUseCase(useCase);		
	}
	public void storeProperty(IUseCase useCase, String name, String value) {
		store.storeProperty(useCase, name, value);		
	}
}
