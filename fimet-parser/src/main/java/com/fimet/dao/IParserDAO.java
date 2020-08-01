package com.fimet.dao;

import java.util.List;

import com.fimet.IManager;
import com.fimet.parser.IEParser;

public interface IParserDAO extends IManager {
	public IEParser findByName(String name);
	public List<IEParser> findAll();
	public IEParser insert(IEParser parser);
	public IEParser update(IEParser parser);
	public IEParser delete(IEParser parser);
}
