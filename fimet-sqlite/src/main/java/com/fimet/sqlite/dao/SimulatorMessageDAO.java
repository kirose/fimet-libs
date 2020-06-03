package com.fimet.sqlite.dao;

import java.sql.SQLException;
import java.util.List;

import com.fimet.dao.ISimulatorMessageDAO;
import com.fimet.simulator.IESimulatorMessage;
import com.fimet.sqlite.simulator.ESimulatorMessage;
import com.fimet.sqlite.simulator.ESimulatorModel;
import com.fimet.utils.CollectionUtils;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

public class SimulatorMessageDAO extends AbstractDAO<ESimulatorMessage,Integer> implements ISimulatorMessageDAO {
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
	public List<ESimulatorMessage> findByIdSimulator(Integer idSimulator) {
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
	@Override
	public List<IESimulatorMessage> getByModelName(String model) {
		ESimulatorModel e = SimulatorModelDAO.getInstance().findByName(model);
		if (e != null) {
			List<ESimulatorMessage> messages = findByIdSimulator(e.getId());
			return CollectionUtils.cast(messages, IESimulatorMessage.class);
		} else {
			return null;
		}
	}
}
