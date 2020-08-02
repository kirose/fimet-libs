package com.fimet;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fimet.FimetLogger;
import com.fimet.IEnviromentManager;
import com.fimet.IEventManager;
import com.fimet.Manager;
import com.fimet.dao.IEnviromentDAO;
import com.fimet.enviroment.Enviroment;
import com.fimet.enviroment.IEEnviroment;
import com.fimet.enviroment.IEnviroment;
import com.fimet.event.EnviromentEvent;
import com.fimet.event.IEnviromentUpdated;
import com.fimet.net.IConnectable;
import com.fimet.net.IConnectionListener;
import com.fimet.utils.ArrayUtils;

public class EnviromentManager implements IEnviromentManager, IEnviromentUpdated, IConnectionListener {

	private IEnviroment active;
	private Map<String, IEnviroment> enviroments;
	private IEventManager eventManager = Manager.get(IEventManager.class);
	public EnviromentManager() {
		eventManager.addListener(EnviromentEvent.ENVIROMENT_UPDATED, this);
		reload(false);
		autostart();
	}
	@Override
	public void start() {
	}
	public void autostart() {
		String name = Manager.getProperty("enviroment.autoconnect");
		if (name != null) {
			IEnviroment enviroment = getEnviroment(name);
			enviroment.connect();
		} else {
			FimetLogger.warning(getClass(),"Cannot found Enviroment "+name);	
		}
	}
	@Override
	public IEnviroment getEnviroment(String name) {
		return enviroments.get(name);
	}
	@Override
	public IEnviroment getActive() {
		return active;
	}

	@Override
	public void connect(IEnviroment enviroment) {
		enviroment.connect();
	}
	@Override
	public void disconnect(IEnviroment enviroment) {
		enviroment.disconnect();
	}

	@Override
	public List<IEnviroment> getEnviroments() {
		return ArrayUtils.copyValuesAsList(enviroments);
	}
	@SuppressWarnings("unchecked")
	private void reload(boolean fireEvent) {
		if (active!=null) {
			active.disconnect();
			active = null;
		}
		enviroments = new ConcurrentHashMap<String, IEnviroment>();
		List<? extends IEEnviroment> all = Manager.get(IEnviromentDAO.class).findAll();
		if (all!=null) {
			for (IEEnviroment e : all) {
				enviroments.put(e.getName(), new Enviroment(e, this));
			}
		}
		if (fireEvent) {
			List<IEnviroment> copy = ArrayUtils.copyValuesAsList(enviroments);
			Manager.get(IEventManager.class).fireEvent(EnviromentEvent.ENVIROMENT_MANAGER_RELOADED,this,copy);
		}
	}
	@Override
	public void reload() {
		reload(true);
	}
	@Override
	public void onEnviromentUpdated(IEEnviroment entity) {
		if (active!= null && active.getName().equals(entity.getName())) {
			Map<String, String> properties = entity.getProperties();
			for (Map.Entry<String, String> e : properties.entrySet()) {
				Manager.getPreferenceStore().setValue(e.getKey(), e.getValue());
			}
		}
	}
	@Override
	public IEnviroment getEnviroment(IEEnviroment entity) {
		if (enviroments.containsKey(entity.getName())) {
			return enviroments.get(entity.getName());
		} else {
			Enviroment enviroment = new Enviroment(entity, this);
			enviroments.put(entity.getName(), enviroment);
			return enviroment;
		}
	}
	@Override
	public void onDisconnected(IConnectable connectable) {
		if (active == connectable) {
			active = null;
		}
	}
	@Override
	public void onConnecting(IConnectable connectable) {
		if (active != null) {
			active.disconnect();
		}
	}
	@Override
	public void onConnected(IConnectable connectable) {
		if (connectable instanceof IEnviroment) {
			if (active != null) {
				active.disconnect();
			}
			active = (IEnviroment)connectable;
		}
	}
}
