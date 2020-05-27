package com.fimet.parser;

import com.fimet.entity.EParser;
import com.fimet.utils.converter.Converter;
import com.fimet.utils.converter.IConverter;
import com.fimet.utils.data.IReader;
import com.fimet.utils.data.IWriter;
import com.fimet.FimetException;
import com.fimet.IFieldGroupManager;
import com.fimet.Manager;


public abstract class BaseMessageParser implements IParser {
	
	/**
	 * The Field Parser Manager
	 */
	private static IFieldGroupManager fieldGroupManager = Manager.get(IFieldGroupManager.class);
	
	/**
	 * Id Parser
	 */
	private Integer idParser;
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
	private String keySequence;
	private IFieldGroup group;
	public BaseMessageParser(EParser entity){
		this.idParser = entity.getId();
		this.converter = Converter.get(entity.getIdConverter());
		this.name = entity.getName();
		this.keySequence = entity.getKeySequence();
		if (entity.getId() != null) {
			this.group = fieldGroupManager.getGroup(entity.getIdGroup());
		} else if (entity.getFieldGroup() != null) {
			this.group = fieldGroupManager.getGroup(entity.getFieldGroup());
		} else {
			throw new FimetException("Parser Initialization exception, invalid parser model "+entity);
		}
	}
	public Integer getId() {
		return idParser;
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
	public String getKeySequence() {
		return keySequence;
	}
	public IConverter getConverter() {
		return converter;
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
}
