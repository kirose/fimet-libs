package com.fimet.sqlite.dao;

import java.sql.SQLException;
import java.util.List;

import com.fimet.sqlite.entity.EStressCycleStore;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

public class StressCycleStoreDAO extends AbstractDAO<EStressCycleStore,Long> {
	private static StressCycleStoreDAO instance;
	public static StressCycleStoreDAO getInstance() {
		if (instance == null) {
			instance = new StressCycleStoreDAO();
		}
		return instance;
	}
	public StressCycleStoreDAO() {
		super();
	}
	public StressCycleStoreDAO(ConnectionSource connection) {
		super(connection);
	}
	public List<EStressCycleStore> findByidTask(Long idTask) {
		try {
			QueryBuilder<EStressCycleStore, Long> qb = getDAO().queryBuilder();
			qb.setWhere(qb.where().eq("idTask", idTask));
			PreparedQuery<EStressCycleStore> preparedQuery = qb.prepare();
			return getDAO().query(preparedQuery);
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}
	public CloseableIterator<EStressCycleStore> iteratorByIdTask(Long idTask) {
		try {
			QueryBuilder<EStressCycleStore, Long> qb = getDAO().queryBuilder();
			qb.setWhere(qb.where().eq("idTask", idTask));
			PreparedQuery<EStressCycleStore> preparedQuery = qb.prepare();
			CloseableIterator<EStressCycleStore> iterator = getDAO().iterator(preparedQuery);
			return iterator;
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}
}
