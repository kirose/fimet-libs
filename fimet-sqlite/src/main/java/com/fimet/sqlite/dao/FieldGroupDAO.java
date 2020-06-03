package com.fimet.sqlite.dao;

import java.sql.SQLException;
import java.util.List;

import com.fimet.dao.IFieldGroupDAO;
import com.fimet.parser.IEFieldGroup;
import com.fimet.sqlite.parser.EFieldGroup;
import com.fimet.utils.CollectionUtils;
import com.j256.ormlite.dao.RawRowMapper;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

public final class FieldGroupDAO extends AbstractDAO<EFieldGroup,Integer> implements IFieldGroupDAO {
	private static FieldGroupDAO instance;
	public static FieldGroupDAO getInstance() {
		if (instance == null) {
			instance = new FieldGroupDAO();
		}
		return instance;
	}
	public FieldGroupDAO() {
		super();
	}
	public FieldGroupDAO(ConnectionSource connection) {
		super(connection);
	}
	public EFieldGroup findByName(String name) {
		try {
			QueryBuilder<EFieldGroup, Integer> qb = getDAO().queryBuilder();
			qb.setWhere(qb.where().eq("name", name));
			qb.limit(1L);
			PreparedQuery<EFieldGroup> preparedQuery = qb.prepare();
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
	@Override
	public IEFieldGroup getByName(String name) {
		return findByName(name);
	}
	@Override
	public List<IEFieldGroup> getAll() {
		return CollectionUtils.cast(findAll(), IEFieldGroup.class);
	}
}
