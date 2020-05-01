package com.fimet.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fimet.IFieldFormatManager;
import com.fimet.IParserManager;
import com.fimet.Manager;
import com.fimet.entity.sqlite.EFieldFormat;
import com.fimet.entity.sqlite.EFieldFormatGroup;
import com.fimet.persistence.dao.FieldFormatDAO;
import com.fimet.persistence.dao.FieldFormatGroupDAO;

public class FieldFormatManager implements IFieldFormatManager {
	
	private FieldFormatGroupDAO dao = FieldFormatGroupDAO.getInstance();
	private FieldFormatDAO daoFieldFormat = FieldFormatDAO.getInstance();
	private Map<Integer, EFieldFormatGroup> cache = new HashMap<>();
	
	public EFieldFormatGroup getGroup(Integer idGroup) {
		if (cache.containsKey(idGroup)) {
			return cache.get(idGroup);
		} else {
			EFieldFormatGroup group = dao.findById(idGroup);
			if (group == null) return null;
			EFieldFormatGroup parent, child = group;
			while (child.getIdParent() != null) {
				child.setParent(parent = getGroup(child.getIdParent()));
				child = parent;
			}
			cache.put(idGroup, group);
			return group;
		}
	}

	public List<EFieldFormat> getFieldsFormat(Integer idGroup) {
		if (getGroup(idGroup) != null) {
			List<EFieldFormat> formats = getFieldsFormat(getGroup(idGroup), new ArrayList<>());
			Collections.sort(formats, (EFieldFormat f1, EFieldFormat f2) ->{return f1.getIdOrder().compareTo(f2.getIdOrder());});
			return formats;
		} else {
			return null;
		}
	}
	private List<EFieldFormat> getFieldsFormat(EFieldFormatGroup group, List<String> exclude){
		List<EFieldFormat> formats = daoFieldFormat.findByGroup(group.getId(), exclude);
		if (group.getParent() != null) {
			if (formats != null && !formats.isEmpty()) {
				for (EFieldFormat format : formats) {
					if (format.getIdFieldParent() == null) {
						if (!exclude.contains(format.getIdField())) {
							exclude.add(format.getIdField());
						}
					}
				}
			}
			List<EFieldFormat> formatsParent = getFieldsFormat(group.getParent(), exclude);
			if (formatsParent != null && !formatsParent.isEmpty()) {
				for (EFieldFormat format : formatsParent) {
					formats.add(format);
				}
			}
		}
		return formats;
	}
	@Override
	public List<EFieldFormatGroup> getRootGroups() {
		List<Integer> ids = dao.findRootIds();
		List<EFieldFormatGroup> roots = new ArrayList<>();
		for (Integer id : ids) {
			roots.add(getGroup(id));
		}
		return roots;
	}
	@Override
	public List<EFieldFormatGroup> getGroups() {
		List<Integer> ids = dao.findAllIds();
		List<EFieldFormatGroup> all = new ArrayList<>();
		for (Integer id : ids) {
			all.add(getGroup(id));
		}
		return all;
	}
	public List<EFieldFormatGroup> getSubGroups(Integer idGroup) {
		List<Integer> ids = dao.findChildIds(idGroup);
		if (ids == null || ids.isEmpty()) {
			return null;
		}
		List<EFieldFormatGroup> subgroups = new ArrayList<>();
		for (Integer id : ids) {
			subgroups.add(getGroup(id));
		}
		return subgroups;
	}
	@Override
	public void free() {
		cache.clear();
	}
	@Override
	public void saveState() {}
	public EFieldFormatGroup saveGroup(EFieldFormatGroup group) {
		FieldFormatGroupDAO.getInstance().insertOrUpdate(group);
		if (group.getId() == null) {
			EFieldFormatGroup last = FieldFormatGroupDAO.getInstance().findLast();
			if (last != null)
				group.setId(last.getId());
		}
		return group;
	}
	public EFieldFormatGroup deleteGroup(EFieldFormatGroup group) {
		FieldFormatGroupDAO.getInstance().delete(group);
		FieldFormatDAO.getInstance().deleteByIdGroup(group.getId());
		return group;
	}
	public EFieldFormat saveField(EFieldFormat field) {
		FieldFormatDAO.getInstance().insertOrUpdate(field);
		if (field.getIdFieldFormat() == null) {
			EFieldFormat last = FieldFormatDAO.getInstance().findLast("idFieldFormat");
			if (last != null)
				field.setIdFieldFormat(last.getIdFieldFormat());
		}
		return field;
	}
	public EFieldFormat deleteField(EFieldFormat field) {
		FieldFormatDAO.getInstance().delete(field);
		return field;
	}

	@Override
	public List<EFieldFormat> getFieldsFormatOnlyGroup(Integer idGroup) {
		return daoFieldFormat.findByGroup(idGroup, null);
	}

	@Override
	public void free(List<Integer> groups) {
		Manager.get(IParserManager.class).free(groups);
		for (Integer idGroup : groups) {
			EFieldFormatGroup group = cache.remove(idGroup);
			if (group != null) {
				group.setParent(null);
			}
		}
	}

	@Override
	public Integer getNextIdGroup() {
		return FieldFormatGroupDAO.getInstance().getNextSequenceId();
	}
	@Override
	public Integer getPrevIdGroup() {
		return FieldFormatGroupDAO.getInstance().getPrevSequenceId();
	}
}
