package com.fimet.persistence.impl;

import java.sql.SQLException;

import com.fimet.persistence.IDataBaseCreator;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class SQLiteCreatorDefault implements IDataBaseCreator {
	ConnectionSource connection;
	@Override
	public void create(ConnectionSource connection) throws SQLException {
		this.connection = connection;
		createTables();
	}
	private void createTables() throws SQLException {
		TableUtils.createTableIfNotExists(connection, com.fimet.entity.sqlite.EUseCaseReport.class);
		TableUtils.createTableIfNotExists(connection, com.fimet.entity.sqlite.EFieldFormatGroup.class);
		TableUtils.createTableIfNotExists(connection, com.fimet.entity.sqlite.EFieldFormat.class);
		TableUtils.createTableIfNotExists(connection, com.fimet.entity.sqlite.ESimulator.class);
		TableUtils.createTableIfNotExists(connection, com.fimet.entity.sqlite.ESimulatorMessage.class);
		TableUtils.createTableIfNotExists(connection, com.fimet.entity.sqlite.ESourceType.class);
		TableUtils.createTableIfNotExists(connection, com.fimet.entity.sqlite.ESource.class);
		TableUtils.createTableIfNotExists(connection, com.fimet.entity.sqlite.EParser.class);
		TableUtils.createTableIfNotExists(connection, com.fimet.entity.sqlite.EPreference.class);
	}
}
