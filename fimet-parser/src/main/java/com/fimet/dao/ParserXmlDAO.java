package com.fimet.dao;

import java.io.File;
import java.util.List;

import com.fimet.FimetException;
import com.fimet.Manager;
import com.fimet.dao.IParserDAO;
import com.fimet.parser.EParserXml;
import com.fimet.utils.XmlUtils;
import com.fimet.utils.converter.Converter;
import com.fimet.xml.ParsersXml;

public class ParserXmlDAO implements IParserDAO<EParserXml> {
	private File file;
	public ParserXmlDAO() {
		String path = Manager.getProperty("parsers.path","fimet/model/parsers.xml");
		if (path == null) {
			throw new PersistenceException("Must declare parsers.path property in fimet.xml");
		}
		file = new File(path);
	}
	@Override
	public EParserXml findByName(String name) {
		if (file.exists()) {
			ParsersXml parsersXml = XmlUtils.fromFile(file, ParsersXml.class);
			List<EParserXml> parsers = validate(parsersXml.getParsers());
			if (parsers != null) {
				for (EParserXml p : parsers) {
					if (name.equals(p.getName())) {
						return p;
					}
				}
			}
		}
		return null;
	}
	@Override
	public List<EParserXml> findAll() {
		if (file.exists()) {
			ParsersXml parsersXml = XmlUtils.fromFile(file, ParsersXml.class);
			List<EParserXml> parsers = validate(parsersXml.getParsers());
			return parsers;
		}
		return null;
	}
	private List<EParserXml> validate(List<EParserXml> parsers) {
		if (parsers != null) {
			for (EParserXml p : parsers) {
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
	public EParserXml insert(EParserXml parser) {
		throw new RuntimeException("Not yet supported");
	}
	@Override
	public EParserXml update(EParserXml parser) {
		throw new RuntimeException("Not yet supported");
	}
	@Override
	public EParserXml delete(EParserXml parser) {
		throw new RuntimeException("Not yet supported");
	}
}
