package com.fimet.sqlite.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.fimet.dao.IParserDAO;
import com.fimet.sqlite.parser.EParser;
import com.fimet.parser.IEParser;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

public class ParserDAO extends AbstractDAO<EParser,Integer> implements IParserDAO {
	private static ParserDAO instance;
	public static ParserDAO getInstance() {
		if (instance == null) {
			instance = new ParserDAO();
		}
		return instance;
	}
	public ParserDAO() {
		super();
	}
	public ParserDAO(ConnectionSource connection) {
		super(connection);
	}
	public EParser findByName(String name){
		try {
			QueryBuilder<EParser, Integer> qb = getDAO().queryBuilder();
			qb.setWhere(qb.where().eq("name", name));
			qb.limit(1L);
			return getDAO().queryForFirst(qb.prepare());
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}
	public List<EParser> findByType(int type){
		try {
			QueryBuilder<EParser, Integer> qb = getDAO().queryBuilder();
			qb.setWhere(qb.where().eq("type", type));
			return getDAO().query(qb.prepare());
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}
	@Override
	public IEParser getByName(String name) {
		return findByName(name);
	}
	@Override
	public List<IEParser> getAll() {
		List<EParser> list = findAll();
		List<IEParser> parsers = new ArrayList<>();
		for (EParser e : list) {
			parsers.add(e);
		}
		return parsers;
	}
}
