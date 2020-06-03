package com.fimet.sqlite.parser;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.fimet.parser.IEParser;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 *
 */
@XmlRootElement(name="parser")
@XmlAccessorType(XmlAccessType.NONE)
@DatabaseTable(tableName = "Parser")
public class EParser implements IEParser {
	@DatabaseField(id = true)
	private Integer id;
	@XmlAttribute(name="name")
	@DatabaseField(canBeNull = false)
	private String name;
	@XmlAttribute(name="class")
	@DatabaseField(canBeNull = false)
	private String parserClass;
	/**
	 * MTI+Header+Bitmap+Fields convert
	 */
	@XmlAttribute(name="converter")
	@DatabaseField(canBeNull = false)
	private String converter;
	@XmlAttribute(name="fieldGroup")
	@DatabaseField(canBeNull = false)
	private String fieldGroup;
	@DatabaseField(canBeNull = false)
	private Integer type;
	@DatabaseField(canBeNull = true)
	private String keySequence;
	public EParser() {}
	public EParser(Integer idParser,
			String name,
			String fieldGroup,
			String parserClass,
			String converter,
			Integer type,
			String keySequence) {
		super();
		this.fieldGroup = fieldGroup;
		this.id = idParser;
		this.name = name;
		this.parserClass = parserClass;
		this.converter = converter;
		this.type = type;
		this.keySequence = keySequence;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer idParser) {
		this.id = idParser;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setFieldGroup(String fieldGroup) {
		this.fieldGroup = fieldGroup;
	}
	public String getFieldGroup() {
		return fieldGroup;
	}
	public String getConverter() {
		return converter;
	}
	public void setConverter(String converter) {
		this.converter = converter;
	}
	public String getParserClass() {
		return parserClass;
	}
	public void setParserClass(String parserClass) {
		this.parserClass = parserClass;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return name;
	}
	public String getKeySequence() {
		return keySequence;
	}
	public void setKeySequence(String keySequence) {
		this.keySequence = keySequence;
	}
}
