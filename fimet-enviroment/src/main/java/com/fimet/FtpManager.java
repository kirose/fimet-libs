package com.fimet;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fimet.dao.IFtpDAO;
import com.fimet.event.EnviromentEvent;
import com.fimet.ftp.Ftp;
import com.fimet.ftp.IEFtp;
import com.fimet.ftp.IFtp;
import com.fimet.utils.ArrayUtils;

public class FtpManager implements IFtpManager {
	private Map<String,IFtp> connections;
	public FtpManager() {
		reload(false);
	}
	@Override
	public IFtp connect(String name) {
		if (connections.containsKey(name)) {
			IFtp ftp = connections.get(name);
			if (ftp.isDisconnected()) {
				if (!ftp.isDisconnected()) {
					ftp.connect();
				}
			}
			return ftp;
		}
		return null;
	}

	@Override
	public IFtp connect(IEFtp entity) {
		if (connections.containsKey(entity.getName())) {
			return connect(entity.getName());
		} else {
			Ftp ftp = new Ftp(entity);
			ftp.connect();
			connections.put(entity.getName(), ftp);
			return ftp;
		}
	}

	@Override
	public IFtp disconnect(String name) {
		if (connections.containsKey(name)) {
			IFtp ftp = connections.get(name);
			if (!ftp.isDisconnected()) {
				ftp.disconnect();
			}
			return ftp;
		}
		return null;
	}

	@Override
	public IFtp disconnect(IFtp connection) {
		connection.disconnect();
		return connection;
	}

	@Override
	public void start() {
	}

	@Override
	public void reload() {
		reload(true);
	}
	private void reload(boolean fireEvent) {
		if (connections!=null&&!connections.isEmpty()) {
			for (Map.Entry<String, IFtp> e : connections.entrySet()) {
				e.getValue().disconnect();
			}
		}
		connections = new ConcurrentHashMap<String,IFtp>();
		IFtpDAO dao = Manager.get(IFtpDAO.class);
		List<IEFtp> all = dao.findAll();
		if (all != null && !all.isEmpty()) {
			for (IEFtp e : all) {
				connections.put(e.getName(), new Ftp(e));
			}
		}
		if (fireEvent) {
			Manager.get(IEventManager.class).fireEvent(EnviromentEvent.FTP_MANAGER_RELOADED, this, getFtps());
		}
	}
	@Override
	public IFtp getFtp(String name) {
		return connections.get(name);
	}
	@Override
	public List<IFtp> getFtps() {
		return ArrayUtils.copyValuesAsList(connections);
	}
	@Override
	public void remove(IFtp ftp) {
		if (connections.containsKey(ftp.getName())) {
			IFtp remove = connections.remove(ftp.getName());
			remove.disconnect();
			Manager.get(IEventManager.class).fireEvent(EnviromentEvent.FTP_REMOVED, this, remove);
		}
	}
	@Override
	public IFtp getFtp(IEFtp entity) {
		if (connections.containsKey(entity.getName())) {
			return connections.get(entity.getName());
		} else {
			Ftp ftp = new Ftp(entity);
			connections.put(entity.getName(), ftp);
			return ftp;
		}
	}

}
