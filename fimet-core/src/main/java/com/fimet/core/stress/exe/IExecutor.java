package com.fimet.core.stress.exe;

import java.util.Map;

import com.fimet.core.net.IMessenger;

public interface IExecutor {
	public Map<IMessenger, MessengerResult> getMessengerResults();
	public Map<IMessenger, InjectorResult> getInjectorResults();
}
