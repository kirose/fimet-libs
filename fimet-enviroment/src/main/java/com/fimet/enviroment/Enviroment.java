package com.fimet.enviroment;

import java.util.Map;
import java.util.Map.Entry;

import com.fimet.IEnviromentManager;
import com.fimet.IEventManager;
import com.fimet.IPreferenceStore;
import com.fimet.Manager;
import com.fimet.event.EnviromentEvent;
import com.fimet.socket.IConnectionListener;

public class Enviroment implements IEnviroment {
	private String name;
	private String type;
	private Map<String, String> properties;
	private Status status;
	private IConnectionListener listener;
	private static IPreferenceStore store;
	public Enviroment(IEEnviroment enviroment) {
		name = enviroment.getName();
		properties = enviroment.getProperties();
		type = enviroment.getType();
		status = Status.DISCONNECTED;
		store = Manager.getPreferenceStore();
	}
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public Status getStatus() {
		return status;
	}

	@Override
	public Map<String, String> getProperties() {
		return properties;
	}

	@Override
	public String getProperty(String name) {
		return properties.get(name);
	}

	@Override
	public String getProperty(String name, String defaultValue) {
		String value = properties.get(name);
		return value != null ? value : defaultValue;
	}
	public void setPropertyValue(String name, Boolean value) {
		properties.put(name, String.valueOf(value));
		store.setValue(name, value);
	}
	
	@Override
	public Boolean getPropertyBoolean(String name) {
		String value = properties.get(name);
		return value != null ? Boolean.valueOf(value) : null;
	}

	@Override
	public Boolean getPropertyBoolean(String name, boolean defaultValue) {
		String value = properties.get(name);
		return value != null ? Boolean.valueOf(value) : defaultValue;
	}
	
	public void setPropertyValue(String name, Integer value) {
		properties.put(name, String.valueOf(value));
		store.setValue(name, value);
	}
	
	@Override
	public Integer getPropertyInteger(String name) {
		String value = properties.get(name);
		return value != null ? Integer.valueOf(value) : null;
	}

	@Override
	public Integer getPropertyInteger(String name, int defaultValue) {
		String value = properties.get(name);
		return value != null ? Integer.valueOf(value) : defaultValue;
	}
	public void setPropertyValue(String name, Long value) {
		properties.put(name, String.valueOf(value));
		store.setValue(name, value);
	}
	
	@Override
	public Long getPropertyLong(String name) {
		String value = properties.get(name);
		return value != null ? Long.valueOf(value) : null;
	}

	@Override
	public Long getPropertyLong(String name, long defaultValue) {
		String value = properties.get(name);
		return value != null ? Long.valueOf(value) : defaultValue;
	}

	@Override
	public void connect() {
		if (status == Status.DISCONNECTED) {
			status = Status.CONNECTING;
			Manager.get(IEventManager.class).fireEvent(EnviromentEvent.ENVIROMENT_CONNECTING, this, this);
			IEnviromentManager enviromentManager = Manager.get(IEnviromentManager.class);
			IEnviroment active = enviromentManager.getActive();
			if (active != null) {
				enviromentManager.disconnect(active);
			}
			if (properties!=null) {
				for (Entry<String, String> e : properties.entrySet()) {
					store.setValue(e.getKey(), e.getValue());
				}
			}
			status = Status.CONNECTED;
			Manager.get(IEventManager.class).fireEvent(EnviromentEvent.ENVIROMENT_CONNECTED, this, this);
		}
	}
	@Override
	public void disconnect() {
		if (status != Status.DISCONNECTED) {
			IEnviromentManager enviromentManager = Manager.get(IEnviromentManager.class);
			IEnviroment active = enviromentManager.getActive();
			if (active != null) {
				enviromentManager.disconnect(active);
			}
			if (properties!=null) {
				for (Entry<String, String> e : properties.entrySet()) {
					store.removePropery(e.getKey());
				}
			}
			status = Status.DISCONNECTED;
			Manager.get(IEventManager.class).fireEvent(EnviromentEvent.ENVIROMENT_DISCONNECTED, this, this);
		}
	}
	@Override
	public boolean isDisconnected() {
		return status==Status.DISCONNECTED;
	}
	@Override
	public boolean isConnected() {
		return status==Status.CONNECTED;
	}
	@Override
	public IConnectionListener getConnectionListener() {
		return listener;
	}
	@Override
	public void setConnectionListener(IConnectionListener listener) {
		this.listener = listener;
	}
	@Override
	public boolean isConnecting() {
		return status == Status.CONNECTING;
	}

}
