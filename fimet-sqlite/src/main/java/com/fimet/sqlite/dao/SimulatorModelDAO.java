package com.fimet.sqlite.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.fimet.dao.ISimulatorModelDAO;
import com.fimet.simulator.IESimulatorModel;
import com.fimet.sqlite.simulator.ESimulatorModel;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

public class SimulatorModelDAO extends AbstractDAO<ESimulatorModel,Integer> implements ISimulatorModelDAO {
	private static SimulatorModelDAO instance;
	public static SimulatorModelDAO getInstance() {
		if (instance == null) {
			instance = new SimulatorModelDAO();
		}
		return instance;
	}
	public SimulatorModelDAO() {
		super();
	}
	public SimulatorModelDAO(ConnectionSource connection) {
		super(connection);
	}
	public List<ESimulatorModel> findAllIssuers() {
		try {
			QueryBuilder<ESimulatorModel,Integer> qb = getDAO().queryBuilder();
			qb.setWhere(qb.where().eq("type", ESimulatorModel.ISSUER));
			PreparedQuery<ESimulatorModel> preparedQuery = qb.prepare();
			return getDAO().query(preparedQuery);
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}	
	}
	public List<ESimulatorModel> findAllAcquirers() {
		try {
			QueryBuilder<ESimulatorModel,Integer> qb = getDAO().queryBuilder();
			qb.setWhere(qb.where().eq("type", ESimulatorModel.ACQUIRER));
			PreparedQuery<ESimulatorModel> preparedQuery = qb.prepare();
			return getDAO().query(preparedQuery);
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}	
	}
	public ESimulatorModel findByName(String name) {
		try {
			QueryBuilder<ESimulatorModel, Integer> qb = getDAO().queryBuilder();
			qb.setWhere(qb.where().eq("name", name));
			qb.limit(1L);
			return qb.queryForFirst();
		} catch (SQLException e) {
			return null;
		}
	}
	public ESimulatorModel findByNameAndType(String name, char type) {
		try {
			QueryBuilder<ESimulatorModel, Integer> qb = getDAO().queryBuilder();
			qb.setWhere(qb.where().eq("name", name).and().eq("type", type));
			qb.limit(1L);
			return qb.queryForFirst();
		} catch (SQLException e) {
			return null;
		}
	}
	@Override
	public IESimulatorModel getByName(String name) {
		return findByName(name);
	}
	@Override
	public List<IESimulatorModel> getAll() {
		List<ESimulatorModel> list = findAll();
		List<IESimulatorModel> entities = new ArrayList<>();
		for (ESimulatorModel e : list) {
			entities.add(e);
		}
		return entities;
	}
}
