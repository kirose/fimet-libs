package com.fimet.exe;

import java.io.File;
import java.util.UUID;

import com.fimet.stress.IStressStore;

public class StressTask extends Task {
	StressMultiResult result;
	IStressStore store;
	public StressTask(Object source) {
		super(UUID.randomUUID(), source, null);
	}
	public StressTask(Object source, File folder) {
		super(UUID.randomUUID(), source, folder);
	}
	public StressTask(UUID id, Object source) {
		super(id, source, null);
	}
	public StressTask(UUID id, Object source, File folder) {
		super(id, source, folder);
	}
	public StressTask(IExecutable exe) {
		super(UUID.randomUUID(), exe, exe.getFolder());
	}
	public void setResult(StressMultiResult result) {
		this.result = result;
	}
	public void setStore(IStressStore store) {
		this.store = store;
	}
	@Override
	public StressMultiResult getResult() {
		return result;
	}
	@Override
	public IStressStore getStore() {
		return store;
	}

}
