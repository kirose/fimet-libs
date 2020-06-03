package com.fimet.parser;

import com.fimet.utils.IReader;
import com.fimet.utils.IWriter;
import com.fimet.utils.converter.Converter;
import com.fimet.utils.converter.IConverter;
import com.fimet.FimetException;
import com.fimet.IFieldGroupManager;
import com.fimet.Manager;


public abstract class BaseMessageParser implements IParser {
	
	/**
	 * The Field Parser Manager
	 */
	private static IFieldGroupManager fieldGroupManager = Manager.get(IFieldGroupManager.class);
	
	/**
	 * Parser Name
	 */
	private String name;
	/**
	 * Global Converter example 
	 * Bancomer: EBCDIC to ASCII
	 * Visa: ASCII to HEX
	 */
	protected final IConverter converter;
	private boolean validateTypes = true;
	private IFieldGroup group;
	public BaseMessageParser(IEParser entity){
		this.name = entity.getName();
		this.converter = Converter.getConverter(entity.getConverter());
		if (entity.getFieldGroup() != null) {
			this.group = fieldGroupManager.getGroup(entity.getFieldGroup());
		} else {
			throw new FimetException("Parser Initialization exception, invalid parser model "+entity);
		}
	}
	public String getName() {
		return name;
	}
	@Override
	public String toString() {
		return name;
	}
	public boolean getValidateTypes() {
		return validateTypes;
	}
	public IFieldGroup getFieldGroup() {
		return group;
	}
	public byte[] parseField(String idField, IMessage message, IReader reader) {
		return group.parse(idField, message, reader);
	}
	public byte[] parseField(int idField, IMessage message, IReader reader) {
		return group.parse(idField, message, reader);
	}
	public void formatField(String idField, IMessage message, IWriter writer) {
		group.format(idField, message, writer);
	}
	public void formatField(int idField, IMessage message, IWriter writer) {
		group.format(idField, message, writer);
	}
	protected IConverter getConverter() {
		return converter;
	}
}
