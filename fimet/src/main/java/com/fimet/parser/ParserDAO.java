package com.fimet.parser;

import java.util.List;

import com.fimet.FimetException;
import com.fimet.Manager;
import com.fimet.dao.IParserDAO;
import com.fimet.utils.CollectionUtils;
import com.fimet.utils.converter.Converter;
import com.fimet.xml.FimetXml;

public class ParserDAO implements IParserDAO {
	public ParserDAO() {}
	@Override
	public IEParser getByName(String name) {
		FimetXml model = Manager.getModel();
		List<EParser> parsers = validate(model.getParsers());
		if (parsers != null) {
			for (EParser p : parsers) {
				if (name.equals(p.getName())) {
					return p;
				}
			}
		}
		return null;
	}
	@Override
	public List<IEParser> getAll() {
		FimetXml model = Manager.getModel();
		return CollectionUtils.cast(validate(model.getParsers()),IEParser.class);
	}
	private List<EParser> validate(List<EParser> parsers) {
		if (parsers != null) {
			for (EParser p : parsers) {
				if (p.getName() == null) 
					throw new FimetException("Name is null for parser "+p);
				if (p.getFieldGroup() == null) 
					throw new FimetException("Field Group is null for parser "+p);
				if (p.getParserClass() == null) 
					throw new FimetException("Parser class is null for parser "+p);
				if (p.getConverter() == null) 
					p.setConverter(Converter.NONE.getName());
			}
		}
		return parsers;
	}
}
