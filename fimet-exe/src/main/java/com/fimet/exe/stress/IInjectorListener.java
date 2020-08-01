package com.fimet.exe.stress;

public interface IInjectorListener {
	void onInjectorFinishInject(IInjector injector);
	void onInjectorStartInject(IInjector injector);
	void onInjectorStartCycle(IInjector injector);
	void onInjectorFinishCycle(IInjector injector);
}
