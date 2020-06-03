package com.fimet.xml;



import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="fieldGroup")
@XmlAccessorType(XmlAccessType.NONE)
public class FieldGroupXml {
	@XmlAttribute(name="name")
	private String name;
	@XmlAttribute(name="path")
	private String path;
	@XmlAttribute(name="parent")
	private String parent;
	public FieldGroupXml() {
		super();
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
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
}
