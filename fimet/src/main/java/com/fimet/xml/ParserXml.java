package com.fimet.xml;



import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="parser")
@XmlAccessorType(XmlAccessType.NONE)
public class ParserXml {
	@XmlAttribute(name="id")
	private String id;
	@XmlAttribute(name="class")
	private String className;
	@XmlAttribute(name="converter")
	private String converter;
	@XmlAttribute(name="fieldGroup")
	private String fieldGroup;
	public ParserXml() {
		super();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getConverter() {
		return converter;
	}
	public void setConverter(String converter) {
		this.converter = converter;
	}
	public String getFieldGroup() {
		return fieldGroup;
	}
	public void setFieldGroup(String fieldGroup) {
		this.fieldGroup = fieldGroup;
	}
}
