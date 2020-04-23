package com.fimet.core.stress.exe;

public interface IExecutorListener {
	void onInjectorFinishCycle(IInjector injector);
	void onInjectorStart(IInjector injector);
	void onInjectorFinish(IInjector injector);
	void onExecutorStart(IExecutor executor);
	void onExecutorFinish(IExecutor executor);
}
