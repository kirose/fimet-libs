package com.fimet.persistence.dao;

import com.fimet.entity.sqlite.EPreference;
import com.j256.ormlite.support.ConnectionSource;

public class PreferenceDAO extends AbstractDAO<EPreference,String> {
	
	private static PreferenceDAO instance;
	public static PreferenceDAO getInstance() {
		if (instance == null) {
			instance = new PreferenceDAO();
		}
		return instance;
	}
	public PreferenceDAO() {
		super();
	}
	public PreferenceDAO(ConnectionSource connection) {
		super(connection);
	}
}
