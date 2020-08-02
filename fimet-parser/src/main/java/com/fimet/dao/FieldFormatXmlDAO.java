package com.fimet.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.fimet.FimetException;
import com.fimet.Manager;
import com.fimet.dao.IFieldFormatDAO;
import com.fimet.parser.EFieldFormatXml;
import com.fimet.parser.EFieldGroupXml;
import com.fimet.parser.IEFieldFormat;
import com.fimet.utils.XmlUtils;
import com.fimet.utils.converter.Converter;
import com.fimet.utils.parser.NumericParser;
import com.fimet.xml.FieldGroupsXml;

public class FieldFormatXmlDAO implements IFieldFormatDAO<EFieldFormatXml> {
	private File file;
	public FieldFormatXmlDAO() {
		String path = Manager.getProperty("fieldGroups.path","fimet/model/fieldGroups.xml");
		if (path == null) {
			throw new PersistenceException("Must declare parsers.path property in fimet.xml");
		}
		file = new File(path);
	}

	@Override
	public List<EFieldFormatXml> findByGroup(String name) {
		if (file.exists()) {
			EFieldGroupXml group = getByName(name);
			if (group.getPath()!=null) {
				EFieldGroupXml g = XmlUtils.fromPath(group.getPath(), EFieldGroupXml.class);
				List<EFieldFormatXml> list = treeToList(g.getFields());
				validate(g, list);
				return list;
			}
		}
		return null;
	}
	private EFieldGroupXml getByName(String name) {
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
	public List<EFieldFormatXml> findAll() {
		List<IEFieldFormat> all = new ArrayList<>();
		FieldGroupsXml groupsXml = XmlUtils.fromFile(file, FieldGroupsXml.class);
		List<EFieldGroupXml> groups = groupsXml.getGroups();
		for (EFieldGroupXml g : groups) {
			if (g.getPath()!=null) {
				EFieldGroupXml group = XmlUtils.fromPath(g.getPath(), EFieldGroupXml.class);
				List<EFieldFormatXml> list = treeToList(group.getFields());
				validate(g, list);
				for (IEFieldFormat f : list) {
					all.add(f);
				}
			}
		}
		return null;
	}
	private List<EFieldFormatXml> treeToList(List<EFieldFormatXml> tree) {
		List<EFieldFormatXml> list = new ArrayList<>(tree.size()+20);
		int i = 0;
		for (EFieldFormatXml node : tree) {
			node.setOrder(String.format("%03d", i++));
			list.add(node);
			if (node.getChildren() != null && !node.getChildren().isEmpty()) {
				childrenToList(list, node, node.getChildren());
			}
		}
		return list;
	}
	private void childrenToList(List<EFieldFormatXml> list, EFieldFormatXml parent, List<EFieldFormatXml> children) {
		StringBuilder s = new StringBuilder();
		int i = 0;
		for (EFieldFormatXml node : children) {
			node.setOrder(parent.getOrder()+"."+String.format("%03d", i++));
			s.append(getKey(node)).append(',');
			node.setIdFieldParent(parent.getIdField());
			list.add(node);
			if (node.getChildren() != null && !node.getChildren().isEmpty()) {
				childrenToList(list, node, node.getChildren());
			}
		}
		s.delete(s.length()-1, s.length());
		parent.setChilds(s.toString());
	}
	private String getKey(EFieldFormatXml node){
		int index = node.getIdField().lastIndexOf('.');
		if (index != -1) {
			return node.getIdField().substring(index+1);
		} else {
			return node.getIdField();
		}
	}
	private void validate(EFieldGroupXml g, List<EFieldFormatXml> list) {
		for (EFieldFormatXml f : list) {
			f.setGroup(g.getName());
			if (f.getIdField()==null)
				throw new FimetException("id field is null for field "+f.getName()+", see group "+g.getName() + " path "+g.getPath());
			if (f.getClassParser() == null)
				throw new FimetException("Parser class is null for field "+f.getIdField()+", see group "+g.getName() + " path "+g.getPath());
			if (f.getLength() == null)
				throw new FimetException("Length is null for field "+f.getIdField()+", see group "+g.getName() + " path "+g.getPath());
			if (f.getConverterValue() == null)
				f.setConverterValue(Converter.NONE.getName());
			if (f.getConverterLength() == null)
				f.setConverterLength(Converter.NONE.getName());
			if (f.getParserLength() == null) 
				f.setParserLength(NumericParser.DEC.getName());
		}
	}

	@Override
	public void start() {
		
	}

	@Override
	public void reload() {
		
	}

	@Override
	public EFieldFormatXml insert(EFieldFormatXml parser) {
		throw new RuntimeException("Not yet supported");
	}

	@Override
	public EFieldFormatXml update(EFieldFormatXml parser) {
		throw new RuntimeException("Not yet supported");
	}

	@Override
	public EFieldFormatXml delete(EFieldFormatXml parser) {
		throw new RuntimeException("Not yet supported");
	}
}
