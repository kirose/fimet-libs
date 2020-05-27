package com.fimet.sqlite;

import java.sql.SQLException;

import com.fimet.entity.EFieldFormat;
import com.fimet.entity.EFieldFormatGroup;
import com.fimet.entity.EParser;
import com.fimet.entity.EPreference;
import com.fimet.entity.ESimulator;
import com.fimet.entity.ESimulatorMessage;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class FimetCreator implements IDataBaseCreator {
	@Override
	public void create(ConnectionSource connection) throws SQLException {
		TableUtils.createTableIfNotExists(connection, EFieldFormatGroup.class);
		TableUtils.createTableIfNotExists(connection, EFieldFormat.class);
		TableUtils.createTableIfNotExists(connection, ESimulator.class);
		TableUtils.createTableIfNotExists(connection, ESimulatorMessage.class);
		TableUtils.createTableIfNotExists(connection, EParser.class);
		TableUtils.createTableIfNotExists(connection, EPreference.class);
	}
}
