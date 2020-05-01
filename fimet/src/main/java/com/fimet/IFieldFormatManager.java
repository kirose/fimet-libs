package com.fimet;

import java.util.List;

import com.fimet.entity.sqlite.EFieldFormat;
import com.fimet.entity.sqlite.EFieldFormatGroup;
/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface IFieldFormatManager extends IManager {
	EFieldFormatGroup getGroup(Integer idGroup);
	List<EFieldFormatGroup> getRootGroups();
	List<EFieldFormatGroup> getGroups();
	List<EFieldFormat> getFieldsFormat(Integer idGroup);
	List<EFieldFormat> getFieldsFormatOnlyGroup(Integer idGroup);
	List<EFieldFormatGroup> getSubGroups(Integer idGroup);
	EFieldFormatGroup saveGroup(EFieldFormatGroup group);
	EFieldFormatGroup deleteGroup(EFieldFormatGroup group);
	EFieldFormat saveField(EFieldFormat field);
	EFieldFormat deleteField(EFieldFormat field);
	void free(List<Integer> groups);
	Integer getNextIdGroup();
	Integer getPrevIdGroup();
}
