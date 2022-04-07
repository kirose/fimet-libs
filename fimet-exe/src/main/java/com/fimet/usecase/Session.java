package com.fimet.usecase;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import com.fimet.Manager;

public class Session implements Delayed {
	private static final long MAX_EXECUTION_TIME = Manager.getPropertyLong("usecase.max-execution-time", 9000L);
	long timeout;
	IUseCase useCase;
	ISessionListener listener;
	Map<String, String> properties;
	public Session(IUseCase useCase, ISessionListener listener) {
		this.listener = listener;
		this.useCase = useCase;
		this.timeout = System.currentTimeMillis() + MAX_EXECUTION_TIME;
	}
	@Override
	public int compareTo(Delayed o) {
		long other = ((Session)o).timeout;
		return timeout < other ? -1 : (timeout == other ? 0 : 1);
	}
	@Override
	public long getDelay(TimeUnit unit) {
        return unit.convert(timeout-System.currentTimeMillis(), TimeUnit.MILLISECONDS);
	}
	public void saveProperty(String key, String value) {
		if (properties == null) {
			properties = new HashMap<String,String>();
		}
		properties.put(key, value);
	}
	public String getProperty(String key) {
		if (properties != null) {
			return properties.get(key);
		}
		return null;
	}
	public IUseCase getUseCase() {
		return useCase;
	}
	public ISessionListener getListener() {
		return listener;
	}
}