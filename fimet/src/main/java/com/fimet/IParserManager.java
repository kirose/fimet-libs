package com.fimet;

import java.util.List;

import com.fimet.commons.history.History;
import com.fimet.entity.sqlite.EParser;
import com.fimet.iso8583.parser.IParser;
/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface IParserManager extends IManager {
	IParser getParser(String name);
	IParser getParser(Integer idParser);
	IParser getParser(EParser entity);
	List<IParser> getParsers();
	void reload(Integer idParser);
	public Class<?>[] getParserClasses();
	public List<EParser> getEntities();
	public List<EParser> getEntities(int type);
	public EParser insert(EParser parser);
	public EParser update(EParser parser);
	public EParser delete(EParser parser);
	public void free(List<Integer> groups);
	public Integer getNextIdParser();
	public Integer getPrevIdParser();
	EParser getEntity(Integer id);
	void commit(History<EParser> history);
}
