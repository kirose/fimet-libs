package com.fimet.parser;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Field Format description for the fields
 * @author Marco Antonio
 *
 */
@XmlRootElement(name="group")
@XmlAccessorType(XmlAccessType.NONE)
public class EFieldGroup implements IEFieldGroup {
	@XmlAttribute(name="name")
	private String name;
	@XmlAttribute(name="path")
	private String path;
	@XmlAttribute(name="parent")
	private String parent;
	@XmlElement(name="field")
	private List<EFieldFormat> fields;
	public EFieldGroup() {}
	public EFieldGroup(String name, String parentName) {
		super();
		this.parent = parentName;
		this.name = name;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public List<EFieldFormat> getFields() {
		return fields;
	}
	public void setFields(List<EFieldFormat> fields) {
		this.fields = fields;
	}
	@Override
	public String toString() {
		return name;
	}
}
