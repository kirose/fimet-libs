package com.fimet.exe;

import java.util.List;

import com.fimet.stress.Stress;
import com.fimet.stress.exe.InjectorResult;

public interface IStressExecutorListener {
	void onInjectorFinishCycle(InjectorResult result);
	void onInjectorStart(InjectorResult result);
	void onInjectorFinish(InjectorResult result);
	void onStressStart(Stress stress);
	void onStressFinish(Stress stress, List<InjectorResult> results);
}
