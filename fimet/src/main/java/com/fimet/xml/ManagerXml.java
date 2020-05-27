package com.fimet.xml;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="manager")
@XmlAccessorType(XmlAccessType.NONE)
//@XmlType(propOrder={"properties"})
public class ManagerXml {
	@XmlAttribute(name="id")
	private String id;
	@XmlAttribute(name="class")
	private String className;
	@XmlAttribute(name="autostart")
	private boolean autostart;
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
	public boolean isAutostart() {
		return autostart;
	}
	public void setAutostart(boolean autostart) {
		this.autostart = autostart;
	}
	public String toString() {
		return id;
	}
}
