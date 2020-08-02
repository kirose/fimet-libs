package com.fimet.dao;

import java.io.File;
import java.util.List;

import com.fimet.Manager;
import com.fimet.dao.IFieldGroupDAO;
import com.fimet.dao.PersistenceException;
import com.fimet.parser.EFieldGroupXml;
import com.fimet.utils.XmlUtils;
import com.fimet.xml.FieldGroupsXml;

public class FieldGroupXmlDAO implements IFieldGroupDAO<EFieldGroupXml> {
	private File file;
	public FieldGroupXmlDAO() {
		String path = Manager.getProperty("fieldGroups.path","fimet/model/fieldGroups.xml");
		if (path == null) {
			throw new PersistenceException("Must declare parsers.path property in fimet.xml");
		}
		file = new File(path);
	}

	@Override
	public EFieldGroupXml findByName(String name) {
		if (file.exists()) {
			FieldGroupsXml groupsXml = XmlUtils.fromFile(file, FieldGroupsXml.class);
			List<EFieldGroupXml> groups = groupsXml.getGroups();
			for (EFieldGroupXml e : groups) {
				if (name.equals(e.getName())) {
					return e;
				}
			}
		}
		return null;
	}

	@Override
	public List<EFieldGroupXml> findAll() {
		if (file.exists()) {
			FieldGroupsXml groupsXml = XmlUtils.fromFile(file, FieldGroupsXml.class);
			List<EFieldGroupXml> groups = groupsXml.getGroups();
			return groups;
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
	public EFieldGroupXml insert(EFieldGroupXml parser) {
		throw new RuntimeException("Not yet supported");
	}

	@Override
	public EFieldGroupXml update(EFieldGroupXml parser) {
		throw new RuntimeException("Not yet supported");
	}

	@Override
	public EFieldGroupXml delete(EFieldGroupXml parser) {
		throw new RuntimeException("Not yet supported");
	}

}
