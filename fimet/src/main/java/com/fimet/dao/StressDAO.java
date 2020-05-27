package com.fimet.dao;


import java.sql.SQLException;
import java.util.List;

import com.fimet.entity.EUseCaseStore;
import com.j256.ormlite.dao.RawRowMapper;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

public class StressDAO extends AbstractDAO<EUseCaseStore,String> {
	private static StressDAO instance;
	public static StressDAO getInstance() {
		if (instance == null) {
			instance = new StressDAO();
		}
		return instance;
	}
	public StressDAO() {
		super();
	}
	public StressDAO(ConnectionSource connection) {
		super(connection);
	}
	public EUseCaseStore findByPath(String path) {
		return findById(path);
	}
	public List<EUseCaseStore> findByProject(String project) {
		try {
			QueryBuilder<EUseCaseStore, String> qb = getDAO().queryBuilder();
			qb.setWhere(qb.where().eq("project", project));
			qb.orderBy("path", true);
			PreparedQuery<EUseCaseStore> preparedQuery = qb.prepare();
			List<EUseCaseStore> result = getDAO().query(preparedQuery);
			return result;
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}
	public List<EUseCaseStore> findByIdJob(Long idJob) {
		try {
			QueryBuilder<EUseCaseStore, String> qb = getDAO().queryBuilder();
			qb.setWhere(qb.where().eq("idJob", idJob));
			qb.orderBy("path", true);
			PreparedQuery<EUseCaseStore> preparedQuery = qb.prepare();
			List<EUseCaseStore> result = getDAO().query(preparedQuery);
			return result;
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}
	public List<String> findProjects() {
		try {
			List<String> projects = getDAO().queryRaw("select project from UseCaseReport group by project", new RawRowMapper<String>() {
			       @Override
			       public String mapRow(String[] columnNames, String[] resultColumns) throws SQLException {
			            return resultColumns[0];
			       }
			}).getResults();
			return projects;
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}
}