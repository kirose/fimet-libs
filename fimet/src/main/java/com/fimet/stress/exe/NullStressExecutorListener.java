package com.fimet.stress.exe;

import java.util.List;

import com.fimet.exe.IStressExecutorListener;
import com.fimet.stress.Stress;

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
	public void onStressStart(Stress stress) {
	}

	@Override
	public void onStressFinish(Stress stress, List<InjectorResult> results) {
	}

}
