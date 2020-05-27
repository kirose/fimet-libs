package com.fimet.dao;

import java.sql.SQLException;
import java.util.List;

import com.fimet.entity.ESimulator;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

public class SimulatorDAO extends AbstractDAO<ESimulator,Integer> {
	private static SimulatorDAO instance;
	public static SimulatorDAO getInstance() {
		if (instance == null) {
			instance = new SimulatorDAO();
		}
		return instance;
	}
	public SimulatorDAO() {
		super();
	}
	public SimulatorDAO(ConnectionSource connection) {
		super(connection);
	}
	public List<ESimulator> findAllIssuers() {
		try {
			QueryBuilder<ESimulator,Integer> qb = getDAO().queryBuilder();
			qb.setWhere(qb.where().eq("type", ESimulator.ISSUER));
			PreparedQuery<ESimulator> preparedQuery = qb.prepare();
			return getDAO().query(preparedQuery);
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}	
	}
	public List<ESimulator> findAllAcquirers() {
		try {
			QueryBuilder<ESimulator,Integer> qb = getDAO().queryBuilder();
			qb.setWhere(qb.where().eq("type", ESimulator.ACQUIRER));
			PreparedQuery<ESimulator> preparedQuery = qb.prepare();
			return getDAO().query(preparedQuery);
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}	
	}
	public ESimulator findByName(String name) {
		try {
			QueryBuilder<ESimulator, Integer> qb = getDAO().queryBuilder();
			qb.setWhere(qb.where().eq("name", name));
			qb.limit(1L);
			return qb.queryForFirst();
		} catch (SQLException e) {
			return null;
		}
	}
	public ESimulator findByNameAndType(String name, char type) {
		try {
			QueryBuilder<ESimulator, Integer> qb = getDAO().queryBuilder();
			qb.setWhere(qb.where().eq("name", name).and().eq("type", type));
			qb.limit(1L);
			return qb.queryForFirst();
		} catch (SQLException e) {
			return null;
		}
	}
}
