package com.fimet.dao;

import java.sql.SQLException;
import java.util.List;

import com.fimet.entity.EFieldFormatGroup;
import com.j256.ormlite.dao.RawRowMapper;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

public final class FieldFormatGroupDAO extends AbstractDAO<EFieldFormatGroup,Integer> {
	private static FieldFormatGroupDAO instance;
	public static FieldFormatGroupDAO getInstance() {
		if (instance == null) {
			instance = new FieldFormatGroupDAO();
		}
		return instance;
	}
	public FieldFormatGroupDAO() {
		super();
	}
	public FieldFormatGroupDAO(ConnectionSource connection) {
		super(connection);
	}
	public EFieldFormatGroup findByName(String name) {
		try {
			QueryBuilder<EFieldFormatGroup, Integer> qb = getDAO().queryBuilder();
			qb.setWhere(qb.where().eq("name", name));
			qb.limit(1L);
			PreparedQuery<EFieldFormatGroup> preparedQuery = qb.prepare();
			return getDAO().queryForFirst(preparedQuery);
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}	
	}
	public List<Integer> findRootIds() {
		try {
			return getDAO().queryRaw("select id from FieldFormatGroup where idParent is null order by id asc", new RawRowMapper<Integer>() {
			       @Override
			       public Integer mapRow(String[] columnNames, String[] resultColumns) throws SQLException {
			            return resultColumns[0] != null ? Integer.parseInt(resultColumns[0]) : null;
			       }
			}).getResults();
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}	
	}
	public List<Integer> findAllIds() {
		try {
			return getDAO().queryRaw("select id from FieldFormatGroup order by id asc", new RawRowMapper<Integer>() {
			       @Override
			       public Integer mapRow(String[] columnNames, String[] resultColumns) throws SQLException {
			            return resultColumns[0] != null ? Integer.parseInt(resultColumns[0]) : null;
			       }
			}).getResults();
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}	
	}
	public List<Integer> findChildIds(Integer idGroup) {
		try {
			return getDAO().queryRaw("select id from FieldFormatGroup where idParent = "+idGroup+" order by id asc", new RawRowMapper<Integer>() {
			       @Override
			       public Integer mapRow(String[] columnNames, String[] resultColumns) throws SQLException {
			            return resultColumns[0] != null ? Integer.parseInt(resultColumns[0]) : null;
			       }
			}).getResults();
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}	
	}
}
