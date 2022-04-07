package com.fimet.usecase;

import com.fimet.exe.IResult;

public class NullFinishListener implements IFinishListener {
	public static final IFinishListener INSTANCE = new NullFinishListener();
	@Override
	public void onFinish(IResult result) {
	}

}
