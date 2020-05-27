package com.fimet.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fimet.FimetException;
import com.fimet.Manager;
import com.fimet.dao.FieldFormatDAO;
import com.fimet.entity.EFieldFormat;
import com.fimet.entity.EFieldFormatGroup;
import com.fimet.utils.data.IReader;
import com.fimet.utils.data.IWriter;

public class FieldGroup implements IFieldGroup {
	static final String loadingMode = Manager.getProperty("parser.modelLoadingMode", "lazy");
	static Comparator<IFieldParser> comparatorIdOrder = (IFieldParser f1, IFieldParser f2)->{
		return f1.getIdOrder().compareTo(f2.getIdOrder());
	}; 
	private Map<String, FieldParserWapper> fields;
	private Integer idGroup;
	private String name;
	private FieldGroup parent;
	private IFieldLoader loader;
	public FieldGroup(String externalName, List<EFieldFormat> externals) {
		fields = new HashMap<String, FieldParserWapper>();
		this.name = externalName;
		for (EFieldFormat f : externals) {
			loadFieldWrapper(f);
		}
		loader = new Earge();
	}
	public FieldGroup(EFieldFormatGroup egroup) {
		fields = new HashMap<String, FieldParserWapper>();
		this.name = egroup.getName();
		this.idGroup = egroup.getId();
		loader = "eager".equalsIgnoreCase(loadingMode) ? new Earge() : new Lazy();
	}
	@Override
	public byte[] parse(int idField, IMessage message, IReader reader) {
		return getFieldParserWrapper(""+idField).parse(reader, message);
	}
	@Override
	public byte[] parse(String idField, IMessage message, IReader reader) {
		return getFieldParserWrapper(idField).parse(reader, message);
	}
	@Override
	public void format(String idField, IMessage message, IWriter writer) {
		getFieldParserWrapper(idField).format(writer, message);
	}
	@Override
	public void format(int idField, IMessage message, IWriter writer) {
		getFieldParserWrapper(""+idField).format(writer, message);
	}
	@Override
	public short[] getAddress(String idField) {
		return getFieldParserWrapper(idField).getAddress();
	}
	void setParent(FieldGroup parent) {
		this.parent = parent;
	}
	public IFieldParser getFieldParser(String idField) {
		return loader.getFieldParserWrapper(idField).getWrapped();
	}
	FieldParserWapper getFieldParserWrapper(String idField) {
		return loader.getFieldParserWrapper(idField);
	}
	FieldParserWapper loadFieldWrapper(EFieldFormat f) {
		try {
			Class<?> classParser = Class.forName(f.getClassParser());
			IFieldParser fieldParser = (IFieldParser) classParser.getConstructor(EFieldFormat.class).newInstance(f);
			FieldParserWapper wrapper = new FieldParserWapper(fieldParser);
			fields.put(f.getIdField(), wrapper);
			return wrapper;
		} catch (Exception e) {
			throw new ParserException("Invalid field parser for group "+name+": "+f.getClassParser(),e);
		}
	}
	void addParentFields() {
		if (parent != null) {
			parent.addParentFields();
			for (Entry<String, FieldParserWapper> e : parent.getFields().entrySet()) {
				if (!fields.containsKey(e.getKey())) {
					fields.put(e.getKey(), e.getValue());
				}
			}
		}
	}
	Map<String, FieldParserWapper> getFields(){
		return fields;
	}
	@Override
	public List<IFieldParser> getRoots() {
		if (loader instanceof Lazy) {
			loadFieldWrappers();
		}
		List<IFieldParser> roots = new ArrayList<IFieldParser>();
		for (Map.Entry<String, FieldParserWapper> e : fields.entrySet()) {
			if (e.getKey().indexOf('.') == -1) {
				roots.add(e.getValue());
				
			}
		}
		Collections.sort(roots, comparatorIdOrder);
		return roots;
	}
	private void loadFieldWrappers() {
		if (fields.isEmpty() && idGroup != null) {
			if (parent != null) {
				parent.loadFieldWrappers();
			}
			List<EFieldFormat> formats = FieldFormatDAO.getInstance().findByGroup(idGroup);
			for (EFieldFormat f : formats) {
				loadFieldWrapper(f);
			}
			addParentFields();
		}
	}
	private interface IFieldLoader {
		FieldParserWapper getFieldParserWrapper(String idField);
	}
	private class Earge implements IFieldLoader {
		@Override
		public FieldParserWapper getFieldParserWrapper(String idField) {
			return fields.get(idField);
		}
	}
	private class Lazy implements IFieldLoader {
		@Override
		public FieldParserWapper getFieldParserWrapper(String idField) {
			if (fields.containsKey(idField)) {
				return fields.get(idField);
			}
			EFieldFormat f = FieldFormatDAO.getInstance().findByGroupAndIdField(idGroup, idField);
			if (f != null) {
				FieldParserWapper fp = loadFieldWrapper(f);
				fields.put(fp.getIdField(), fp);
				return fp;
			}
			if (parent != null){
				FieldParserWapper fp = parent.getFieldParserWrapper(idField);
				if (fp != null) {
					fields.put(fp.getIdField(), fp);
					return fp;
				}
			}
			throw new ParserException("Unkown field "+idField+" for group "+name);
		}
	}
	public void reload() {
		if (loader instanceof Lazy) {
			FieldFormatDAO dao = FieldFormatDAO.getInstance();
			for (Entry<String, FieldParserWapper> e : fields.entrySet()) {
				reloadField(e.getValue(), dao.findByGroupAndIdField(idGroup, e.getKey()));
			}
		} else {
			if (idGroup != null) {
				List<EFieldFormat> all = FieldFormatDAO.getInstance().findByGroup(idGroup);
				for (EFieldFormat f : all) {
					if (fields.containsKey(f.getIdField())) {
						reloadField(fields.get(f.getIdField()), f);
					}
				}
			} else {
				throw new FimetException("No implemented yet");
			}
		}
	}
	FieldParserWapper reloadField(FieldParserWapper wrapper, EFieldFormat f) {
		try {
			Class<?> classParser = Class.forName(f.getClassParser());
			IFieldParser fieldParser = (IFieldParser) classParser.getConstructor(EFieldFormat.class).newInstance(f);
			wrapper.setWrapped(fieldParser);
			return wrapper;
		} catch (Exception e) {
			throw new ParserException("Reload exception for field parser in group "+name+": "+wrapper.getWrapped().getClass(),e);
		}
	}
	@Override
	public String toString() {
		return name;
	}
	@Override
	public String getName() {
		return name;
	}
}
