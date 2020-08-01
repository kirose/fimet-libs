package com.fimet.dao;

import java.io.File;
import java.util.List;

import com.fimet.Manager;
import com.fimet.dao.IFieldGroupDAO;
import com.fimet.dao.PersistenceException;
import com.fimet.parser.EFieldGroup;
import com.fimet.parser.IEFieldGroup;
import com.fimet.utils.CollectionUtils;
import com.fimet.utils.XmlUtils;
import com.fimet.xml.FieldGroupsXml;

public class FieldGroupXmlDAO implements IFieldGroupDAO {
	private File file;
	public FieldGroupXmlDAO() {
		String path = Manager.getProperty("fieldGroups.path","fimet/model/fieldGroups.xml");
		if (path == null) {
			throw new PersistenceException("Must declare parsers.path property in fimet.xml");
		}
		file = new File(path);
	}

	@Override
	public IEFieldGroup findByName(String name) {
		if (file.exists()) {
			FieldGroupsXml groupsXml = XmlUtils.fromFile(file, FieldGroupsXml.class);
			List<EFieldGroup> groups = groupsXml.getGroups();
			for (EFieldGroup e : groups) {
				if (name.equals(e.getName())) {
					return e;
				}
			}
		}
		return null;
	}

	@Override
	public List<IEFieldGroup> findAll() {
		if (file.exists()) {
			FieldGroupsXml groupsXml = XmlUtils.fromFile(file, FieldGroupsXml.class);
			List<EFieldGroup> groups = groupsXml.getGroups();
			return CollectionUtils.cast(groups,IEFieldGroup.class);
		}
		return null;
	}

	@Override
	public void start() {
	}

	@Override
	public void reload() {
	}

	@Override
	public IEFieldGroup insert(IEFieldGroup parser) {
		throw new RuntimeException("Not yet supported");
	}

	@Override
	public IEFieldGroup update(IEFieldGroup parser) {
		throw new RuntimeException("Not yet supported");
	}

	@Override
	public IEFieldGroup delete(IEFieldGroup parser) {
		throw new RuntimeException("Not yet supported");
	}

}
