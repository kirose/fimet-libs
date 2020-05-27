package com.fimet.sqlite;

import java.sql.SQLException;

import com.j256.ormlite.support.ConnectionSource;

public interface IDataBaseCreator {
	public void create(ConnectionSource connection) throws SQLException;
}
