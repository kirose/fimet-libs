package com.fimet.stress;

import java.util.List;

import com.fimet.exe.SocketResult;

public interface IStressMonitor {
	void onInjectorFinishCycle(SocketResult result);
	void onInjectorStart(SocketResult result);
	void onInjectorFinish(SocketResult result);
	void onStressStart(IStress stress);
	void onStressFinish(IStress stress, List<SocketResult> results);
}
