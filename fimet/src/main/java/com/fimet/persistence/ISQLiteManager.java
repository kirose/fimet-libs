package com.fimet.persistence;

import com.fimet.IManager;
import com.j256.ormlite.support.ConnectionSource;

public interface ISQLiteManager extends IManager {
	public ConnectionSource getFimetConnection();
	public ConnectionSource getConnection(String name);
	public void connect();
	public void disconnect();
}
