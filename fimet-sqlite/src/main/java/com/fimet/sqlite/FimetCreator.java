package com.fimet.sqlite;

import java.sql.SQLException;

import com.fimet.sqlite.parser.EFieldFormat;
import com.fimet.sqlite.parser.EFieldGroup;
import com.fimet.sqlite.parser.EParser;
import com.fimet.sqlite.simulator.ESimulatorModel;
import com.fimet.sqlite.simulator.ESimulator;
import com.fimet.sqlite.simulator.ESimulatorMessage;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class FimetCreator implements IDataBaseCreator {
	@Override
	public void create(ConnectionSource connection) throws SQLException {
		TableUtils.createTableIfNotExists(connection, EFieldGroup.class);
		TableUtils.createTableIfNotExists(connection, EFieldFormat.class);
		//TableUtils.createTableIfNotExists(connection, ESocket.class);
		TableUtils.createTableIfNotExists(connection, ESimulator.class);
		TableUtils.createTableIfNotExists(connection, ESimulatorModel.class);
		TableUtils.createTableIfNotExists(connection, ESimulatorMessage.class);
		TableUtils.createTableIfNotExists(connection, EParser.class);
	}
}
