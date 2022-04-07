package com.fimet.exe;

import com.fimet.stress.IStress;

public interface IStressListener {
	void onStressStart(IStress stress);
	void onStressFinish(IStress stress);
	void onStressError(IStress stress, Exception e);
}
