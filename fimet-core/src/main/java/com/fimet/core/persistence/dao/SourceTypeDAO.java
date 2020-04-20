package com.fimet.core.persistence.dao;


import java.sql.SQLException;

import com.fimet.commons.exception.PersistenceException;
import com.fimet.core.entity.sqlite.SourceType;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

public class SourceTypeDAO extends AbstractDAO<SourceType,Integer> {
	private static SourceTypeDAO instance;
	public static SourceTypeDAO getInstance() {
		if (instance == null) {
			instance = new SourceTypeDAO();
		}
		return instance;
	}
	public SourceTypeDAO() {
		super();
	}
	public SourceTypeDAO(ConnectionSource connection) {
		super(connection);
	}
	public SourceType findByClass(Class<?> clazz) {
		try {
			QueryBuilder<SourceType, Integer> qb = getDAO().queryBuilder();
			qb.setWhere(qb.where().eq("interfaceClass", clazz));
			qb.limit(1L);
			PreparedQuery<SourceType> preparedQuery = qb.prepare();
			return getDAO().queryForFirst(preparedQuery);
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

}
