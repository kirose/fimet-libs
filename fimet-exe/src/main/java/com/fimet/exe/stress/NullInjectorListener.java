package com.fimet.exe.stress;

public class NullInjectorListener implements IInjectorListener {
	public static final IInjectorListener INSTANCE = new NullInjectorListener();
	@Override
	public void onInjectorStartInject(IInjector injector) {}

	@Override
	public void onInjectorFinishInject(IInjector injector) {}

	@Override
	public void onInjectorStartCycle(IInjector injector) {}

	@Override
	public void onInjectorFinishCycle(IInjector injector) {}

}
