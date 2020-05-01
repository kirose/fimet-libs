package com.fimet.utils;

import com.fimet.IParserManager;
import com.fimet.Manager;
import com.fimet.commons.converter.Converter;
import com.fimet.commons.converter.IConverter;
import com.fimet.iso8583.parser.IParser;

public class ParserBuilder {

	private static IParserManager parserManager = Manager.get(IParserManager.class);
	private com.fimet.entity.sqlite.EParser entity;

	public ParserBuilder() {
		super();
		entity= new com.fimet.entity.sqlite.EParser();
		entity.setName("Dinamyc Build");
		entity.setIdGroup(4);
		entity.setIdConverter(Converter.NONE.getId());
		entity.setType(IParser.ISO8583);
	}
	public ParserBuilder id(int idParser) {
		entity.setId(idParser);
		return this;
	}
	public ParserBuilder group(int id) {
		entity.setIdGroup(id);
		return this;
	}
	public ParserBuilder converter(IConverter converter) {
		entity.setIdConverter(converter.getId());
		return this;
	}
	public ParserBuilder clazz(Class<? extends IParser> parserClass) {
		entity.setId(entity.hashCode());
		entity.setName("Dinamyc Build "+parserClass.getSimpleName());
		entity.setParserClass(parserClass.getName());
		return this;
	}
	public ParserBuilder type(Integer type) {
		entity.setType(type);
		return this;
	}
	public IParser create() {
		return parserManager.getParser(entity);
	}
}
