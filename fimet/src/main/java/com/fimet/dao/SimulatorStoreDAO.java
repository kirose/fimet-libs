package com.fimet.dao;

import java.sql.SQLException;
import java.util.List;

import com.fimet.entity.ESimulatorStore;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

public class SimulatorStoreDAO extends AbstractDAO<ESimulatorStore,Long> {
	private static SimulatorStoreDAO instance;
	public static SimulatorStoreDAO getInstance() {
		if (instance == null) {
			instance = new SimulatorStoreDAO();
		}
		return instance;
	}
	public SimulatorStoreDAO() {
		super();
	}
	public SimulatorStoreDAO(ConnectionSource connection) {
		super(connection);
	}
	public List<ESimulatorStore> findByidTask(Long idTask) {
		try {
			QueryBuilder<ESimulatorStore, Long> qb = getDAO().queryBuilder();
			qb.setWhere(qb.where().eq("idTask", idTask));
			PreparedQuery<ESimulatorStore> preparedQuery = qb.prepare();
			return getDAO().query(preparedQuery);
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}
	public CloseableIterator<ESimulatorStore> iteratorByIdTask(Long idTask) {
		try {
			QueryBuilder<ESimulatorStore, Long> qb = getDAO().queryBuilder();
			qb.setWhere(qb.where().eq("idTask", idTask));
			PreparedQuery<ESimulatorStore> preparedQuery = qb.prepare();
			CloseableIterator<ESimulatorStore> iterator = getDAO().iterator(preparedQuery);
			return iterator;
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}
}
