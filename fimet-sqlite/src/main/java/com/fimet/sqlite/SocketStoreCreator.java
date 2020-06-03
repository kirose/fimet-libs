package com.fimet.sqlite;

import java.sql.SQLException;

import com.fimet.sqlite.entity.ESocketStore;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class SocketStoreCreator implements IDataBaseCreator {
	@Override
	public void create(ConnectionSource connection) throws SQLException {
		TableUtils.createTableIfNotExists(connection, ESocketStore.class);
	}
}
