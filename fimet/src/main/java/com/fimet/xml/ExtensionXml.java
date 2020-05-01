package com.fimet.xml;



import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="extension")
@XmlAccessorType(XmlAccessType.NONE)
public class ExtensionXml {
	@XmlAttribute(name="id")
	private String id;
	@XmlAttribute(name="class")
	private String className;
	public ExtensionXml() {
		super();
	}
	public ExtensionXml(String id, String className) {
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
	public String toString() {
		return id;
	}
}
