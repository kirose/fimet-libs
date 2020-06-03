package com.fimet.simulator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="add")
@XmlAccessorType(XmlAccessType.NONE)
public class SimulatorField implements Cloneable {
	@XmlAttribute(name="id")
	private String idField;
	@XmlAttribute(name="value")
	private String value;
	@XmlAttribute(name="class")
	private String className;
	public SimulatorField() {
		super();
	}
	public SimulatorField(String idField, String value) {
		super();
		this.idField = idField;
		this.value = value;
	}
	public SimulatorField(String idField, Class<?> clazz) {
		super();
		this.idField = idField;
		this.className = clazz.getName();
	}
	public String getIdField() {
		return idField;
	}
	public void setIdField(String idField) {
		this.idField = idField;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public SimulatorField clone() throws CloneNotSupportedException {
		return (SimulatorField) super.clone();
	}
}