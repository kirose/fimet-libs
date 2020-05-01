package com.fimet.stress.exe;

public interface IInjectorListener {
	void onInjectorFinishInject(IInjector injector);
	void onInjectorStartInject(IInjector injector);
	void onInjectorStartCycle(IInjector injector);
	void onInjectorFinishCycle(IInjector injector);
}
