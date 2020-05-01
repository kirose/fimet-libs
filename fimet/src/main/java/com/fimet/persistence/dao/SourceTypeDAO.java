package com.fimet.persistence.dao;


import java.sql.SQLException;

import com.fimet.commons.exception.PersistenceException;
import com.fimet.entity.sqlite.ESourceType;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

public class SourceTypeDAO extends AbstractDAO<ESourceType,Integer> {
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
	public ESourceType findByClass(Class<?> clazz) {
		try {
			QueryBuilder<ESourceType, Integer> qb = getDAO().queryBuilder();
			qb.setWhere(qb.where().eq("interfaceClass", clazz));
			qb.limit(1L);
			PreparedQuery<ESourceType> preparedQuery = qb.prepare();
			return getDAO().queryForFirst(preparedQuery);
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

}
