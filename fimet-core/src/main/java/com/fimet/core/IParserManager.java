package com.fimet.core;

import java.util.List;

import com.fimet.commons.history.History;
import com.fimet.core.entity.sqlite.Parser;
import com.fimet.core.iso8583.parser.IParser;
/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface IParserManager extends IManager {
	IParser getParser(String name);
	IParser getParser(Integer idParser);
	IParser getParser(Parser entity);
	List<IParser> getParsers();
	void reload(Integer idParser);
	public Class<?>[] getParserClasses();
	public List<Parser> getEntities();
	public List<Parser> getEntities(int type);
	public Parser insert(Parser parser);
	public Parser update(Parser parser);
	public Parser delete(Parser parser);
	public void free(List<Integer> groups);
	public Integer getNextIdParser();
	public Integer getPrevIdParser();
	Parser getEntity(Integer id);
	void commit(History<Parser> history);
}
