package com.fimet.dao;

import java.util.List;

import com.fimet.parser.IEParser;

public interface IParserDAO {
	public IEParser getByName(String name);
	public List<IEParser> getAll();
}
