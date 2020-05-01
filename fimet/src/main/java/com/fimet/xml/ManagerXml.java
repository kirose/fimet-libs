package com.fimet.xml;


import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="manager")
@XmlAccessorType(XmlAccessType.NONE)
//@XmlType(propOrder={"properties"})
public class ManagerXml {
	@XmlAttribute(name="id")
	private String id;
	@XmlAttribute(name="class")
	private String className;
	@XmlElement(name="extension")
	private List<ExtensionXml> extension;
	public ManagerXml() {
		super();
	}
	public ManagerXml(String id, String className) {
		super();
		this.id = id;
		this.className = className;
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
	public List<ExtensionXml> getExtensions() {
		return extension;
	}
	public void setExtensions(List<ExtensionXml> extension) {
		this.extension = extension;
	}
	public String toString() {
		return id;
	}
}
