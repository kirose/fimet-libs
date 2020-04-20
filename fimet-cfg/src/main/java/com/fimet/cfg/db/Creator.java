package com.fimet.cfg.db;


import java.io.IOException;
import java.sql.SQLException;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class Creator {
	ConnectionSource connection;
	public Creator() throws SQLException {
		connection = new JdbcConnectionSource("jdbc:sqlite:resources/fimet.db");
	}
	public Creator createDataBase() {
		try {
	
//			TableUtils.createTableIfNotExists(connection, com.fimet.core.entity.sqlite.UseCaseReport.class);
//			TableUtils.createTableIfNotExists(connection, com.fimet.core.entity.sqlite.Socket.class);
//			
			new FieldFormatCreator(connection).create().insertAll();
			new SimulatorCreator(connection).create().insertAll();
			new ParserCreator(connection).create().insertAll();
			new PreferencesCreator(connection).create().insertAll().insertAll();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	public Creator close() throws IOException {
		if (connection != null)
			connection.close();
		return this;
	}
	public static void main(String[] args) throws Exception {
		new Creator()
		.createDataBase()
		.close();
	}
}
