package com.fimet.parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.fimet.AbstractManager;
import com.fimet.FimetException;
import com.fimet.IFieldGroupManager;
import com.fimet.Manager;
import com.fimet.dao.IFieldGroupDAO;

public class FieldGroupManager extends AbstractManager implements IFieldGroupManager {
	
	final String mode = Manager.getProperty("fieldGroup.mode", "lazy");
	
	private Map<String, FieldGroup> mapNameGroup;
	private IFieldGroupDAO dao = Manager.getExtension(IFieldGroupDAO.class, FieldGroupDAO.class);
	public FieldGroupManager() {
		mapNameGroup = new HashMap<String, FieldGroup>();
		loadGroups();
	}
	public void start() {
		loadFields();
	}
	public void reload() {
		mapNameGroup.clear();
		loadGroups();
		loadFields();
	}
	private void loadFields() {
		if ("eager".equalsIgnoreCase(mode)) {
			Set<FieldGroup> roots = getRootGroups();
			for (FieldGroup f : roots) {
				f.reload();
			}
		}
	}
	private void loadGroups() {
		List<IEFieldGroup> allGroups = dao.getAll();
		for (IEFieldGroup g : allGroups) {
			FieldGroup fg = new FieldGroup(g);
			mapNameGroup.put(g.getName(), fg);
		}
		for (IEFieldGroup g : allGroups) {
			if (g.getParent() != null) {
				FieldGroup parent = mapNameGroup.get(g.getParent());
				FieldGroup child = mapNameGroup.get(g.getName());
				child.setParent(parent);
				parent.addChild(child);
			}
		}
	}
	private Set<FieldGroup> getRootGroups() {
		Set<FieldGroup> roots = new HashSet<>();
		for (Entry<String, FieldGroup> e : mapNameGroup.entrySet()) {
			if (e.getValue().getParent() == null) {
				roots.add(e.getValue());
			}
		}
		return roots;
	}
	@Override
	public FieldGroup getGroup(String name) {
		if (mapNameGroup.containsKey(name)) {
			return mapNameGroup.get(name);
		} else {
			throw new FimetException("Unkown field group "+name);
		}
	}
	@Override
	public void reload(String name) {
		mapNameGroup.get(name).reload();
	}

}
