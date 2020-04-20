package com.fimet.core.persistence.impl;

import java.sql.SQLException;

import com.fimet.core.persistence.IDataBaseCreator;
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
		TableUtils.createTableIfNotExists(connection, com.fimet.core.entity.sqlite.UseCaseReport.class);
		TableUtils.createTableIfNotExists(connection, com.fimet.core.entity.sqlite.FieldFormatGroup.class);
		TableUtils.createTableIfNotExists(connection, com.fimet.core.entity.sqlite.FieldFormat.class);
		TableUtils.createTableIfNotExists(connection, com.fimet.core.entity.sqlite.Simulator.class);
		TableUtils.createTableIfNotExists(connection, com.fimet.core.entity.sqlite.SimulatorMessage.class);
		TableUtils.createTableIfNotExists(connection, com.fimet.core.entity.sqlite.SourceType.class);
		TableUtils.createTableIfNotExists(connection, com.fimet.core.entity.sqlite.Source.class);
		TableUtils.createTableIfNotExists(connection, com.fimet.core.entity.sqlite.Parser.class);
		TableUtils.createTableIfNotExists(connection, com.fimet.core.entity.sqlite.Preference.class);
	}
}
