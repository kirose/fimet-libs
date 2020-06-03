package com.fimet.sqlite;

import java.sql.SQLException;

import com.fimet.sqlite.entity.ESimulatorStore;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class SimulatorStoreCreator implements IDataBaseCreator {
	@Override
	public void create(ConnectionSource connection) throws SQLException {
		TableUtils.createTableIfNotExists(connection, ESimulatorStore.class);
	}
}
