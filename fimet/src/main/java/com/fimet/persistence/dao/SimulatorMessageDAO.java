package com.fimet.persistence.dao;

import java.sql.SQLException;
import java.util.List;

import com.fimet.commons.exception.PersistenceException;
import com.fimet.entity.sqlite.ESimulatorMessage;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

public class SimulatorMessageDAO extends AbstractDAO<ESimulatorMessage,Integer> {
	private static SimulatorMessageDAO instance;
	public static SimulatorMessageDAO getInstance() {
		if (instance == null) {
			instance = new SimulatorMessageDAO();
		}
		return instance;
	}
	public SimulatorMessageDAO() {
		super();
	}
	public SimulatorMessageDAO(ConnectionSource connection) {
		super(connection);
	}
	public ESimulatorMessage findRequestByMti(Integer idSimulator, String mti) {
		try {
			QueryBuilder<ESimulatorMessage, Integer> qb = getDAO().queryBuilder();
			qb.setWhere(qb.where().eq("idSimulator", idSimulator).and().eq("mti", mti).and().eq("type", ESimulatorMessage.REQUEST));
			qb.limit(1L);
			PreparedQuery<ESimulatorMessage> preparedQuery = qb.prepare();
			return getDAO().queryForFirst(preparedQuery);
		} catch (SQLException e) {
			throw new PersistenceException("Cannot find SimulatorMessage",e);
		}
	}
	public ESimulatorMessage findResponseByMti(Integer idSimulator, String mti) {
		try {
			QueryBuilder<ESimulatorMessage, Integer> qb = getDAO().queryBuilder();
			qb.setWhere(qb.where().eq("idSimulator", idSimulator).and().eq("mti", mti).and().eq("type", ESimulatorMessage.RESPONSE));
			qb.limit(1L);
			PreparedQuery<ESimulatorMessage> preparedQuery = qb.prepare();
			return getDAO().queryForFirst(preparedQuery);
		} catch (SQLException e) {
			throw new PersistenceException("Cannot find SimulatorMessage",e);
		}
	}
	public List<ESimulatorMessage> findByIdSmulator(Integer idSimulator) {
		try {
			QueryBuilder<ESimulatorMessage, Integer> qb = getDAO().queryBuilder();
			qb.setWhere(qb.where().eq("idSimulator", idSimulator));
			PreparedQuery<ESimulatorMessage> preparedQuery = qb.prepare();
			return getDAO().query(preparedQuery);
		} catch (SQLException e) {
			throw new PersistenceException("Cannot find SimulatorMessage",e);
		}
	}
	public int deleteByIdSimulator(Integer idSimulator) {
		try {
			DeleteBuilder<ESimulatorMessage, Integer> db = getDAO().deleteBuilder();
			db.setWhere(db.where().eq("idSimulator", idSimulator));
			return db.delete();
		} catch (SQLException e) {
			throw new PersistenceException("Cannot delete SimulatorMessage",e);
		}		
	}
}
