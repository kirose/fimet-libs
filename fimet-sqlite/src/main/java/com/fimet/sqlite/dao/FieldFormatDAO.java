package com.fimet.sqlite.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.fimet.dao.IFieldFormatDAO;
import com.fimet.parser.IEFieldFormat;
import com.fimet.sqlite.parser.EFieldFormat;
import com.j256.ormlite.dao.RawRowMapper;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;

public final class FieldFormatDAO extends AbstractDAO<EFieldFormat,Integer>
implements IFieldFormatDAO {
	private static FieldFormatDAO instance;
	public static FieldFormatDAO getInstance() {
		if (instance == null) {
			instance = new FieldFormatDAO();
		}
		return instance;
	}
	public FieldFormatDAO() {
		super();
	}
	public FieldFormatDAO(ConnectionSource connection) {
		super(connection);
	}
	public List<EFieldFormat> findByGroup(Integer idGroup, List<String> exclude) {
		try {
			QueryBuilder<EFieldFormat, Integer> qb = getDAO().queryBuilder();
			Where<EFieldFormat, Integer> where = qb.where().eq("idGroup", idGroup);
			if (exclude != null && !exclude.isEmpty()) {
				where = where.and().notIn("idField", exclude);
				/*for (String i : exclude) {
					where = where.and().not().like("idField", i+".%");
				}*/
			}
			qb.setWhere(where);
			qb.orderBy("order", true);
			//qb.orderBy("idField", true);
			PreparedQuery<EFieldFormat> preparedQuery = qb.prepare();
			return getDAO().query(preparedQuery);
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}	
	}
	public List<EFieldFormat> findByGroup(Integer idGroup) {
		try {
			QueryBuilder<EFieldFormat, Integer> qb = getDAO().queryBuilder();
			qb.setWhere(qb.where().eq("idGroup", idGroup));
			qb.orderBy("order", true);
			//qb.orderBy("idField", true);
			PreparedQuery<EFieldFormat> preparedQuery = qb.prepare();
			return getDAO().query(preparedQuery);
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}	
	}
	public List<EFieldFormat> findByGroup(String group) {
		try {
			QueryBuilder<EFieldFormat, Integer> qb = getDAO().queryBuilder();
			qb.setWhere(qb.where().eq("group", group));
			qb.orderBy("order", true);
			//qb.orderBy("idField", true);
			PreparedQuery<EFieldFormat> preparedQuery = qb.prepare();
			return getDAO().query(preparedQuery);
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}	
	}
	public EFieldFormat findByGroupAndIdField(String group, String idField) {
		try {
			QueryBuilder<EFieldFormat, Integer> qb = getDAO().queryBuilder();
			qb.setWhere(qb.where().eq("group", group).and().eq("idField", idField));
			qb.limit(1L);
			PreparedQuery<EFieldFormat> preparedQuery = qb.prepare();
			return getDAO().queryForFirst(preparedQuery);
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}	
	}
	public Integer findIdFieldFormat(String group, String idField) {
		try {
			Integer idFieldFormat = getDAO().queryRaw("select id from FieldFormat where \"group\" = '"+group+"' and idField = '"+idField+"'", new RawRowMapper<Integer>() {
			       @Override
			       public Integer mapRow(String[] columnNames, String[] resultColumns) throws SQLException {
			            return resultColumns[0] != null ? Integer.parseInt(resultColumns[0]) : null;
			       }
			}).getFirstResult();
			return idFieldFormat;
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}	
	}
	public EFieldFormat insertOrUpdate(EFieldFormat entity) {
		try {
			if (entity.getId() != null) {
				getDAO().update(entity);
			} else {
				Integer id = findIdFieldFormat(entity.getGroup(), entity.getIdField());
				if (id != null) {
					entity.setId(id);
					getDAO().update(entity);
				} else {
					getDAO().create(entity);
				}
			}
			return entity;
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}
	public int deleteByGroup(String group) {
		try {
			DeleteBuilder<EFieldFormat, Integer> db = getDAO().deleteBuilder();
			db.setWhere(db.where().eq("group", group));
			return db.delete();
		} catch (SQLException e) {
			throw new PersistenceException("Cannot delete FieldFormatGroup "+group,e);
		}
	}
	@Override
	public List<IEFieldFormat> getByGroup(String group) {
		List<EFieldFormat> list = findByGroup(group);
		List<IEFieldFormat> fields = new ArrayList<>(list.size());
		for (EFieldFormat e : list) {
			fields.add(e);
		}
		return fields;
	}
	@Override
	public List<IEFieldFormat> getAll() {
		List<EFieldFormat> list = findAll();
		List<IEFieldFormat> fields = new ArrayList<>(list.size());
		for (EFieldFormat e : list) {
			fields.add(e);
		}
		return fields;
	}
}
