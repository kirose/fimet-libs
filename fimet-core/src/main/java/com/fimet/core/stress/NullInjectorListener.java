package com.fimet.core.stress;

import com.fimet.core.stress.exe.IInjector;
import com.fimet.core.stress.exe.IInjectorListener;

public class NullInjectorListener implements IInjectorListener {
	public static final IInjectorListener INSTANCE = new NullInjectorListener();
	@Override
	public void onInjectorStartInject(IInjector injector) {}

	@Override
	public void onInjectorFinishInject(IInjector injector) {}

}
