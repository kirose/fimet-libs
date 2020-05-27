package com.fimet;

import com.fimet.parser.IFieldGroup;
/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface IFieldGroupManager extends IManager {
	IFieldGroup getGroup(String name);
	IFieldGroup getGroup(Integer idGroup);
	void reload(String name);
	void reload(Integer idGroup);
}
