package com.fimet.core;

import java.util.List;


import com.fimet.commons.exception.CompilationException;
import com.fimet.commons.history.History;
import com.fimet.core.entity.sqlite.Source;
import com.fimet.core.entity.sqlite.SourceType;
import com.fimet.core.listener.ISourceListener;
/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface ISourceManager extends IManager {
	List<SourceType> getSourcesTypes();
	SourceType getSourceType(Integer id);
	SourceType getSourceType(Class<?> clazz);
	Source getSource(Integer id);
	Source insert(Source source);
	Source update(Source source);
	Source delete(Source source);
	Source findByClassName(String className);
	List<Source> findByType(Integer idType);
	List<Source> findByType(Class<?> clazz);
	Integer getNextIdSorce();
	Integer getPrevIdSource();
	void compile(Source source) throws CompilationException;
	void compile(List<Source> sources) throws CompilationException;
	void install(List<Source> sources);
	void install(Source source);
	Object newInstance(Source source);
	Object newInstance(String className);
	Object newInstance(Integer idSource);
	void commit(History<Source> history);
	String getSourceTemplate(SourceType selected);
	List<Source> getEntities();
	void addFirstListener(int type, ISourceListener listener);
	void addListener(int type, ISourceListener listener);
	void removeListener(int type, ISourceListener listener);
	
}
