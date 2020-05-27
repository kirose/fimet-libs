package com.fimet.dao;

import java.sql.SQLException;
import java.util.List;

import com.fimet.entity.EStressStore;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

public class StressStoreDAO extends AbstractDAO<EStressStore,Long> {
	private static StressStoreDAO instance;
	public static StressStoreDAO getInstance() {
		if (instance == null) {
			instance = new StressStoreDAO();
		}
		return instance;
	}
	public StressStoreDAO() {
		super();
	}
	public StressStoreDAO(ConnectionSource connection) {
		super(connection);
	}
	public List<EStressStore> findByidTask(Long idTask) {
		try {
			QueryBuilder<EStressStore, Long> qb = getDAO().queryBuilder();
			qb.setWhere(qb.where().eq("idTask", idTask));
			PreparedQuery<EStressStore> preparedQuery = qb.prepare();
			return getDAO().query(preparedQuery);
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}
	public CloseableIterator<EStressStore> iteratorByIdTask(Long idTask) {
		try {
			QueryBuilder<EStressStore, Long> qb = getDAO().queryBuilder();
			qb.setWhere(qb.where().eq("idTask", idTask));
			PreparedQuery<EStressStore> preparedQuery = qb.prepare();
			CloseableIterator<EStressStore> iterator = getDAO().iterator(preparedQuery);
			return iterator;
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}
}
