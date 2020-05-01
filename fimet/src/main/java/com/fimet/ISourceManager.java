package com.fimet;

import java.util.List;


import com.fimet.commons.exception.CompilationException;
import com.fimet.commons.history.History;
import com.fimet.entity.sqlite.ESource;
import com.fimet.entity.sqlite.ESourceType;
import com.fimet.listener.ISourceListener;
/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface ISourceManager extends IManager {
	List<ESourceType> getSourcesTypes();
	ESourceType getSourceType(Integer id);
	ESourceType getSourceType(Class<?> clazz);
	ESource getSource(Integer id);
	ESource insert(ESource source);
	ESource update(ESource source);
	ESource delete(ESource source);
	ESource findByClassName(String className);
	List<ESource> findByType(Integer idType);
	List<ESource> findByType(Class<?> clazz);
	Integer getNextIdSorce();
	Integer getPrevIdSource();
	void compile(ESource source) throws CompilationException;
	void compile(List<ESource> sources) throws CompilationException;
	void install(List<ESource> sources);
	void install(ESource source);
	Object newInstance(ESource source);
	Object newInstance(String className);
	Object newInstance(Integer idSource);
	void commit(History<ESource> history);
	String getSourceTemplate(ESourceType selected);
	List<ESource> getEntities();
	void addFirstListener(int type, ISourceListener listener);
	void addListener(int type, ISourceListener listener);
	void removeListener(int type, ISourceListener listener);
	
}
