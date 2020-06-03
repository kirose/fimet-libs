package com.fimet;

import java.util.List;

import com.fimet.parser.IEParser;
import com.fimet.parser.IParser;
/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface IParserManager extends IManager {
	IParser getParser(String name);
	IParser getParser(IEParser entity);
	List<IParser> getParsers();
	void reload(String parser);
	public Class<?>[] getParserClasses();
}
