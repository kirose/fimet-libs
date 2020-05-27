package com.fimet.sqlite;

import java.sql.SQLException;

import com.fimet.entity.EStressStore;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class StressStoreCreator implements IDataBaseCreator {
	@Override
	public void create(ConnectionSource connection) throws SQLException {
		TableUtils.createTableIfNotExists(connection, EStressStore.class);
		//TableUtils.createTableIfNotExists(connection, EStressCycleStore.class);
	}
}
