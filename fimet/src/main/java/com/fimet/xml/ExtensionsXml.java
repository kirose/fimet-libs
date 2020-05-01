package com.fimet.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="extensions")
@XmlAccessorType(XmlAccessType.NONE)
public class ExtensionsXml {
	@XmlElement(name="extension")
	private List<ManagerXml> managers;
	public ExtensionsXml() {
		super();
	}
	public ExtensionsXml(List<ManagerXml> managers) {
		super();
		this.managers = managers;
	}
	public List<ManagerXml> getManagers() {
		return managers;
	}
	public void setManagers(List<ManagerXml> managers) {
		this.managers = managers;
	}
}
