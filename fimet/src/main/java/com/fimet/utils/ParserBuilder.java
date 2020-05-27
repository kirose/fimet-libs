package com.fimet.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.fimet.FimetException;
import com.fimet.IFieldGroupManager;
import com.fimet.Manager;
import com.fimet.entity.EParser;
import com.fimet.parser.IFieldGroup;
import com.fimet.parser.IParser;
import com.fimet.parser.ParserException;
import com.fimet.utils.converter.Converter;
import com.fimet.utils.converter.IConverter;

public class ParserBuilder {

	private com.fimet.entity.EParser entity;
	private Class<?> parserClass;

	public ParserBuilder() {
		super();
		entity= new com.fimet.entity.EParser();
		entity.setIdGroup(4);
		entity.setIdConverter(Converter.NONE.getId());
		entity.setType(IParser.ISO8583);
	}
	public ParserBuilder id(int idParser) {
		entity.setId(idParser);
		return this;
	}
	public ParserBuilder name(String name) {
		entity.setName(name);
		return this;
	}
	public ParserBuilder fieldGroup(String name) {
		IFieldGroup group = Manager.get(IFieldGroupManager.class).getGroup(name);
		if (group == null) {
			throw new FimetException("Unkown field group "+name);
		}
		entity.setFieldGroup(group.getName());
		return this;
	}
	public ParserBuilder fieldGroup(int id) {
		entity.setIdGroup(id);
		return this;
	}
	public ParserBuilder converter(IConverter converter) {
		entity.setIdConverter(converter.getId());
		return this;
	}
	public ParserBuilder parserClass(Class<? extends IParser> parserClass) {
		this.parserClass = parserClass;
		entity.setParserClass(parserClass.getName());
		return this;
	}
	public ParserBuilder type(Integer type) {
		entity.setType(type);
		return this;
	}
	public IParser build() {
		if (parserClass == null) {
			throw new ParserException("Parser class may not be null");
		}
		if (entity.getName() == null) {
			entity.setName(parserClass.getSimpleName());
		}
		return newParser(entity);
	}
	private IParser newParser(EParser entity) {
		try {
			Constructor<?> constructor = parserClass.getConstructor(com.fimet.entity.EParser.class);
			return (IParser) constructor.newInstance(entity);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new ParserException("Invalid Parser class: " + entity.getParserClass(),e);
		} catch (NoSuchMethodException e) {
			throw new ParserException("No found public constructor with "+EParser.class.getName()+" as argument in class: " + entity.getParserClass(),e);
		} catch (SecurityException e) {
			throw new ParserException("Invalid Parser class: " + entity.getParserClass(),e);
		} catch (IllegalArgumentException | InvocationTargetException e) {
			throw new ParserException("Invalid Parser class: " + entity.getParserClass(),e);
		}
	}
}
