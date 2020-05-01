package com.fimet.stress.exe;

public class NullExecutorListener implements IExecutorListener {
	public static final NullExecutorListener INSTANCE = new NullExecutorListener();

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
	public void onExecutorStart(IExecutor executor) {
	}

	@Override
	public void onExecutorFinish(IExecutor executor) {
	}

}
