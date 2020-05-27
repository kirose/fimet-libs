package com.fimet.dao;


import com.fimet.entity.ECodeStore;
import com.j256.ormlite.support.ConnectionSource;

public class CodeStoreDAO extends AbstractDAO<ECodeStore,String> {
	private static CodeStoreDAO instance;
	public static CodeStoreDAO getInstance() {
		if (instance == null) {
			instance = new CodeStoreDAO();
		}
		return instance;
	}
	public CodeStoreDAO() {
		super();
	}
	public CodeStoreDAO(ConnectionSource connection) {
		super(connection);
	}
}