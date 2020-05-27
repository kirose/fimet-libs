package com.fimet.exe.usecase;

public class NullFinishListener implements IFinishListener {
	public static final IFinishListener INSTANCE = new NullFinishListener();
	@Override
	public void onFinish(Object source) {
	}

}
