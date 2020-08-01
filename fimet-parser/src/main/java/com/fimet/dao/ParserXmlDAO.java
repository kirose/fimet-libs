package com.fimet.dao;

import java.io.File;
import java.util.List;

import com.fimet.FimetException;
import com.fimet.Manager;
import com.fimet.dao.IParserDAO;
import com.fimet.parser.EParser;
import com.fimet.parser.IEParser;
import com.fimet.utils.CollectionUtils;
import com.fimet.utils.XmlUtils;
import com.fimet.utils.converter.Converter;
import com.fimet.xml.ParsersXml;

public class ParserXmlDAO implements IParserDAO {
	private File file;
	public ParserXmlDAO() {
		String path = Manager.getProperty("parsers.path","fimet/model/parsers.xml");
		if (path == null) {
			throw new PersistenceException("Must declare parsers.path property in fimet.xml");
		}
		file = new File(path);
	}
	@Override
	public IEParser findByName(String name) {
		if (file.exists()) {
			ParsersXml parsersXml = XmlUtils.fromFile(file, ParsersXml.class);
			List<EParser> parsers = validate(parsersXml.getParsers());
			if (parsers != null) {
				for (EParser p : parsers) {
					if (name.equals(p.getName())) {
						return p;
					}
				}
			}
		}
		return null;
	}
	@Override
	public List<IEParser> findAll() {
		if (file.exists()) {
			ParsersXml parsersXml = XmlUtils.fromFile(file, ParsersXml.class);
			List<EParser> parsers = validate(parsersXml.getParsers());
			return CollectionUtils.cast(parsers,IEParser.class);
		}
		return null;
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
	@Override
	public void start() {
	}
	@Override
	public void reload() {
	}
	@Override
	public IEParser insert(IEParser parser) {
		throw new RuntimeException("Not yet supported");
	}
	@Override
	public IEParser update(IEParser parser) {
		throw new RuntimeException("Not yet supported");
	}
	@Override
	public IEParser delete(IEParser parser) {
		throw new RuntimeException("Not yet supported");
	}
}
