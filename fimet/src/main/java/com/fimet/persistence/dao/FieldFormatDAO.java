package com.fimet.persistence.dao;

import java.sql.SQLException;
import java.util.List;

import com.fimet.commons.exception.PersistenceException;
import com.fimet.entity.sqlite.EFieldFormat;
import com.j256.ormlite.dao.RawRowMapper;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedDelete;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;

public final class FieldFormatDAO extends AbstractDAO<EFieldFormat,Integer> {
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
			qb.orderBy("idOrder", true);
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
			qb.orderBy("idOrder", true);
			//qb.orderBy("idField", true);
			PreparedQuery<EFieldFormat> preparedQuery = qb.prepare();
			return getDAO().query(preparedQuery);
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}	
	}
	public EFieldFormat findByGroupAndIdField(Integer idGroup, String idField) {
		try {
			QueryBuilder<EFieldFormat, Integer> qb = getDAO().queryBuilder();
			qb.setWhere(qb.where().eq("idGroup", idGroup).and().eq("idField", idField));
			qb.limit(1L);
			PreparedQuery<EFieldFormat> preparedQuery = qb.prepare();
			return getDAO().queryForFirst(preparedQuery);
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}	
	}
	public int deleteByParser(Integer idParser) {
		try {
			DeleteBuilder<EFieldFormat, Integer> db = getDAO().deleteBuilder();
			db.setWhere(db.where().eq("idParser", idParser));
			PreparedDelete<EFieldFormat> prepare = db.prepare();
			return getDAO().delete(prepare);
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}	
	}
	public Integer findIdFieldFormat(Integer idGroup, String idField) {
		try {
			Integer idFieldFormat = getDAO().queryRaw("select idFieldFormat from FieldFormat where idGroup = "+idGroup+" and idField = '"+idField+"'", new RawRowMapper<Integer>() {
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
			if (entity.getIdFieldFormat() != null) {
				getDAO().update(entity);
			} else {
				Integer id = findIdFieldFormat(entity.getIdGroup(), entity.getIdField());
				if (id != null) {
					entity.setIdFieldFormat(id);
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
	public int deleteByIdGroup(Integer idGroup) {
		try {
			DeleteBuilder<EFieldFormat, Integer> db = getDAO().deleteBuilder();
			db.setWhere(db.where().eq("idGroup", idGroup));
			return db.delete();
		} catch (SQLException e) {
			throw new PersistenceException("Cannot delete FieldFormatGroup "+idGroup,e);
		}
	}
}
