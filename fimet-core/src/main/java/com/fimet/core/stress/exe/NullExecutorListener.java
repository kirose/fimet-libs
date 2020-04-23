package com.fimet.core.stress.exe;

public class NullExecutorListener implements IExecutorListener {
	public static final NullExecutorListener INSTANCE = new NullExecutorListener();
	@Override
	public void onInjectorStart(IInjector injector) {}

	@Override
	public void onInjectorFinish(IInjector injector) {}

	@Override
	public void onExecutorStart(IExecutor executor) {}

	@Override
	public void onExecutorFinish(IExecutor executor) {}

	@Override
	public void onInjectorFinishCycle(IInjector injector) {}

}
