package com.fimet.exe.stress;

import java.util.List;

import com.fimet.exe.InjectorResult;
import com.fimet.stress.IStress;
import com.fimet.stress.IStressExecutorListener;

public class NullStressExecutorListener implements IStressExecutorListener {
	public static final NullStressExecutorListener INSTANCE = new NullStressExecutorListener();

	@Override
	public void onInjectorFinishCycle(InjectorResult result) {
	}

	@Override
	public void onInjectorStart(InjectorResult result) {
	}

	@Override
	public void onInjectorFinish(InjectorResult result) {
	}

	@Override
	public void onStressStart(IStress stress) {
	}

	@Override
	public void onStressFinish(IStress stress, List<InjectorResult> results) {
	}

}
