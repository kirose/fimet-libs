package com.fimet.dao;

import java.sql.SQLException;
import java.util.List;

import com.fimet.entity.ESocketStore;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

public class SocketStoreDAO extends AbstractDAO<ESocketStore,Long> {
	private static SocketStoreDAO instance;
	public static SocketStoreDAO getInstance() {
		if (instance == null) {
			instance = new SocketStoreDAO();
		}
		return instance;
	}
	public SocketStoreDAO() {
		super();
	}
	public SocketStoreDAO(ConnectionSource connection) {
		super(connection);
	}
	public List<ESocketStore> findByidTask(Long idTask) {
		try {
			QueryBuilder<ESocketStore, Long> qb = getDAO().queryBuilder();
			qb.setWhere(qb.where().eq("idTask", idTask));
			PreparedQuery<ESocketStore> preparedQuery = qb.prepare();
			return getDAO().query(preparedQuery);
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}
	public CloseableIterator<ESocketStore> iteratorByIdTask(Long idTask) {
		try {
			QueryBuilder<ESocketStore, Long> qb = getDAO().queryBuilder();
			qb.setWhere(qb.where().eq("idTask", idTask));
			PreparedQuery<ESocketStore> preparedQuery = qb.prepare();
			CloseableIterator<ESocketStore> iterator = getDAO().iterator(preparedQuery);
			return iterator;
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}
}
