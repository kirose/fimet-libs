package com.fimet.core.persistence.dao;

import java.sql.SQLException;
import java.util.List;

import com.fimet.commons.exception.PersistenceException;
import com.fimet.core.entity.sqlite.Source;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

public class SourceDAO extends AbstractDAO<Source,Integer> {
	private static SourceDAO instance;
	public static SourceDAO getInstance() {
		if (instance == null) {
			instance = new SourceDAO();
		}
		return instance;
	}
	public SourceDAO() {
		super();
	}
	public SourceDAO(ConnectionSource connection) {
		super(connection);
	}
	public Source findByClassName(String className) {
		try {
			QueryBuilder<Source, Integer> qb = getDAO().queryBuilder();
			qb.setWhere(qb.where().eq("className", className));
			qb.limit(1L);
			PreparedQuery<Source> preparedQuery = qb.prepare();
			return getDAO().queryForFirst(preparedQuery);
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}
	public List<Source> findByType(Integer idType) {
		try {
			QueryBuilder<Source, Integer> qb = getDAO().queryBuilder();
			qb.setWhere(qb.where().eq("type", idType));
			PreparedQuery<Source> preparedQuery = qb.prepare();
			return getDAO().query(preparedQuery);
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}
}
