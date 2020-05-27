package com.fimet.dao;

import java.sql.SQLException;
import java.util.List;

import com.fimet.entity.EUseCaseStore;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

public class UseCaseStoreDAO extends AbstractDAO<EUseCaseStore,Long> {
	private static UseCaseStoreDAO instance;
	public static UseCaseStoreDAO getInstance() {
		if (instance == null) {
			instance = new UseCaseStoreDAO();
		}
		return instance;
	}
	public UseCaseStoreDAO() {
		super();
	}
	public UseCaseStoreDAO(ConnectionSource connection) {
		super(connection);
	}
	public List<EUseCaseStore> findByidTask(Long idTask) {
		try {
			QueryBuilder<EUseCaseStore, Long> qb = getDAO().queryBuilder();
			qb.setWhere(qb.where().eq("idTask", idTask));
			PreparedQuery<EUseCaseStore> preparedQuery = qb.prepare();
			return getDAO().query(preparedQuery);
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}
	public CloseableIterator<EUseCaseStore> iteratorByIdTask(Long idTask) {
		try {
			QueryBuilder<EUseCaseStore, Long> qb = getDAO().queryBuilder();
			qb.setWhere(qb.where().eq("idTask", idTask));
			PreparedQuery<EUseCaseStore> preparedQuery = qb.prepare();
			CloseableIterator<EUseCaseStore> iterator = getDAO().iterator(preparedQuery);
			return iterator;
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}
}
