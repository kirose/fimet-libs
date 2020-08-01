package com.fimet.exe.stress;

import java.util.List;

import com.fimet.exe.SocketResult;
import com.fimet.stress.IStress;
import com.fimet.stress.IStressMonitor;

public class NullStressMonitor implements IStressMonitor {
	public static final NullStressMonitor INSTANCE = new NullStressMonitor();

	@Override
	public void onInjectorFinishCycle(SocketResult result) {
	}

	@Override
	public void onInjectorStart(SocketResult result) {
	}

	@Override
	public void onInjectorFinish(SocketResult result) {
	}

	@Override
	public void onStressStart(IStress stress) {
	}

	@Override
	public void onStressFinish(IStress stress, List<SocketResult> results) {
	}

}
