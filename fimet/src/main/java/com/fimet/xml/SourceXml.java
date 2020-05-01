package com.fimet.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="source")
@XmlAccessorType(XmlAccessType.NONE)
public class SourceXml {
	@XmlAttribute(name="name")
	private String name;
	@XmlAttribute(name="path")
	private String path;
	public SourceXml() {
		super();
	}
	public SourceXml(String path) {
		super();
		this.path = path;
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
	
}
