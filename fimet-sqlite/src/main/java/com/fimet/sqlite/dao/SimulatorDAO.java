package com.fimet.sqlite.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.fimet.dao.ISimulatorDAO;
import com.fimet.simulator.IESimulator;
import com.fimet.sqlite.simulator.ESimulator;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

public class SimulatorDAO extends AbstractDAO<ESimulator,Integer> implements ISimulatorDAO {
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
	@Override
	public IESimulator getByName(String name) {
		return findByName(name);
	}
	@Override
	public List<IESimulator> getAll() {
		List<ESimulator> list = findAll();
		List<IESimulator> parsers = new ArrayList<>();
		for (ESimulator e : list) {
			parsers.add(e);
		}
		return parsers;
	}
}
