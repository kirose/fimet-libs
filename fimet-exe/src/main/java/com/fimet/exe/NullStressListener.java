package com.fimet.exe;

import com.fimet.stress.IStress;

public class NullStressListener implements IStressListener {
	public static final NullStressListener INSTANCE = new NullStressListener();

	@Override
	public void onStressStart(IStress useCase) {
	}

	@Override
	public void onStressFinish(IStress useCase) {
	}

	@Override
	public void onStressError(IStress stress, Exception e) {
	}
}
