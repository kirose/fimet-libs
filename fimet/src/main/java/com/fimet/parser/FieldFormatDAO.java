package com.fimet.parser;

import java.util.ArrayList;
import java.util.List;

import com.fimet.FimetException;
import com.fimet.Manager;
import com.fimet.dao.IFieldFormatDAO;
import com.fimet.utils.CollectionUtils;
import com.fimet.utils.XmlUtils;
import com.fimet.utils.converter.Converter;
import com.fimet.utils.parser.NumericParser;
import com.fimet.xml.FimetXml;

public class FieldFormatDAO implements IFieldFormatDAO {

	public FieldFormatDAO() {
	}

	@Override
	public List<IEFieldFormat> getByGroup(String name) {
		EFieldGroup group = getByName(name);
		if (group.getPath()!=null) {
			EFieldGroup g = XmlUtils.fromPath(group.getPath(), EFieldGroup.class);
			List<EFieldFormat> list = treeToList(g.getFields());
			validate(g, list);
			return CollectionUtils.cast(list, IEFieldFormat.class);
		}
		return null;
	}
	private EFieldGroup getByName(String name) {
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
	public List<IEFieldFormat> getAll() {
		FimetXml model = Manager.getModel();
		List<IEFieldFormat> all = new ArrayList<>();
		List<EFieldGroup> fieldGroups = model.getFieldGroups();
		for (EFieldGroup g : fieldGroups) {
			if (g.getPath()!=null) {
				EFieldGroup group = XmlUtils.fromPath(g.getPath(), EFieldGroup.class);
				List<EFieldFormat> list = treeToList(group.getFields());
				validate(g, list);
				for (IEFieldFormat f : list) {
					all.add(f);
				}
			}
		}
		return null;
	}
	private List<EFieldFormat> treeToList(List<EFieldFormat> tree) {
		List<EFieldFormat> list = new ArrayList<>(tree.size()+20);
		int i = 0;
		for (EFieldFormat node : tree) {
			node.setOrder(String.format("%03d", i++));
			list.add(node);
			if (node.getChildren() != null && !node.getChildren().isEmpty()) {
				childrenToList(list, node, node.getChildren());
			}
		}
		return list;
	}
	private void childrenToList(List<EFieldFormat> list, EFieldFormat parent, List<EFieldFormat> children) {
		StringBuilder s = new StringBuilder();
		int i = 0;
		for (EFieldFormat node : children) {
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
	private String getKey(EFieldFormat node){
		int index = node.getIdField().lastIndexOf('.');
		if (index != -1) {
			return node.getIdField().substring(index+1);
		} else {
			return node.getIdField();
		}
	}
	private void validate(EFieldGroup g, List<EFieldFormat> list) {
		for (EFieldFormat f : list) {
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
}
