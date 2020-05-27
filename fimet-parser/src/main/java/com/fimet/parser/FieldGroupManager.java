package com.fimet.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fimet.AbstractManager;
import com.fimet.FimetException;
import com.fimet.IFieldGroupManager;
import com.fimet.Manager;
import com.fimet.dao.FieldFormatDAO;
import com.fimet.dao.FieldFormatGroupDAO;
import com.fimet.entity.EFieldFormat;
import com.fimet.entity.EFieldFormatGroup;

public class FieldGroupManager extends AbstractManager implements IFieldGroupManager {
	
	final String loadingMode = Manager.getProperty("fieldGroup.mode", "lazy");
	
	private Map<String, FieldGroup> groups;
	private Map<Integer, String> mapIdToName;
	public FieldGroupManager() {
		groups = new HashMap<String, FieldGroup>();
		mapIdToName = new HashMap<Integer, String>();
		loadGroups();
	}
	private void loadGroups() {
		List<EFieldFormatGroup> allGroups = FieldFormatGroupDAO.getInstance().findAll();
		for (EFieldFormatGroup g : allGroups) {
			groups.put(g.getName(), new FieldGroup(g));
			mapIdToName.put(g.getId(), g.getName());
		}
		for (EFieldFormatGroup g : allGroups) {
			if (g.getIdParent() != null) {
				groups.get(g.getName()).setParent(groups.get(mapIdToName.get(g.getIdParent())));
			}
		}
	}
	@Override
	public FieldGroup getGroup(Integer idGroup) {
		if (mapIdToName.containsKey(idGroup)) {
			return groups.get(mapIdToName.get(idGroup));
		} else {
			throw new FimetException("Unkown field group identified by id "+idGroup);
		}
	}
	@Override
	public FieldGroup getGroup(String name) {
		if (groups.containsKey(name)) {
			return groups.get(name);
		} else {
			throw new FimetException("Unkown field group "+name);
		}
	}
	@Override
	public void reload(String name) {
		groups.get(name).reload();
	}
	@Override
	public void reload(Integer idGroup) {
		reload(mapIdToName.get(idGroup));
	}
	@Override
	public void start() {
		if ("eager".equalsIgnoreCase(loadingMode)) {
			List<EFieldFormat> allFields = FieldFormatDAO.getInstance().findAll();
			for (EFieldFormat f : allFields) {
				groups.get(mapIdToName.get(f.getIdGroup())).loadFieldWrapper(f);
			}
			for (Entry<String, FieldGroup> e : groups.entrySet()) {
				e.getValue().addParentFields();
			}
		}
	}
}
