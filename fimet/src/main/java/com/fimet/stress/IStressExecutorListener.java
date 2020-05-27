package com.fimet.stress;

import java.util.List;

import com.fimet.exe.InjectorResult;

public interface IStressExecutorListener {
	void onInjectorFinishCycle(InjectorResult result);
	void onInjectorStart(InjectorResult result);
	void onInjectorFinish(InjectorResult result);
	void onStressStart(IStress stress);
	void onStressFinish(IStress stress, List<InjectorResult> results);
}
