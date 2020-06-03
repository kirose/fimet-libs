package com.fimet.parser;

import java.util.List;

import com.fimet.Manager;
import com.fimet.dao.IFieldGroupDAO;
import com.fimet.utils.CollectionUtils;
import com.fimet.xml.FimetXml;

public class FieldGroupDAO implements IFieldGroupDAO {

	public FieldGroupDAO() {}

	@Override
	public IEFieldGroup getByName(String name) {
		FimetXml model = Manager.getModel();
		List<EFieldGroup> groups = model.getFieldGroups();
		for (EFieldGroup e : groups) {
			if (name.equals(e.getName())) {
				return e;
			}
		}
		return null;
	}

	@Override
	public List<IEFieldGroup> getAll() {
		FimetXml model = Manager.getModel();
		return CollectionUtils.cast(model.getFieldGroups(),IEFieldGroup.class);
	}

}
