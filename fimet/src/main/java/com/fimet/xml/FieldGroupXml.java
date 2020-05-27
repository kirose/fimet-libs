package com.fimet.xml;



import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="parser")
@XmlAccessorType(XmlAccessType.NONE)
public class FieldGroupXml {
	@XmlAttribute(name="id")
	private String id;
	@XmlAttribute(name="path")
	private String path;
	public FieldGroupXml() {
		super();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
}
