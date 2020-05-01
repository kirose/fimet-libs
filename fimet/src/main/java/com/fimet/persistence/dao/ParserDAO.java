package com.fimet.persistence.dao;

import java.sql.SQLException;
import java.util.List;

import com.fimet.commons.exception.PersistenceException;
import com.fimet.entity.sqlite.EParser;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

public class ParserDAO extends AbstractDAO<EParser,Integer> {
	private static ParserDAO instance;
	public static ParserDAO getInstance() {
		if (instance == null) {
			instance = new ParserDAO();
		}
		return instance;
	}
	public ParserDAO() {
		super();
	}
	public ParserDAO(ConnectionSource connection) {
		super(connection);
	}
	public EParser findByName(String name){
		try {
			QueryBuilder<EParser, Integer> qb = getDAO().queryBuilder();
			qb.setWhere(qb.where().eq("name", name));
			qb.limit(1L);
			return getDAO().queryForFirst(qb.prepare());
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}
	public List<EParser> findByType(int type){
		try {
			QueryBuilder<EParser, Integer> qb = getDAO().queryBuilder();
			qb.setWhere(qb.where().eq("type", type));
			return getDAO().query(qb.prepare());
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

}
