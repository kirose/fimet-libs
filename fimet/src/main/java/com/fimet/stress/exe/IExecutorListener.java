package com.fimet.stress.exe;

public interface IExecutorListener {
	void onInjectorFinishCycle(InjectorResult result);
	void onInjectorStart(InjectorResult result);
	void onInjectorFinish(InjectorResult result);
	void onExecutorStart(IExecutor executor);
	void onExecutorFinish(IExecutor executor);
}
