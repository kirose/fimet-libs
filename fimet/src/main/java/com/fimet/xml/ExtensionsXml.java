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
	private List<ExtensionXml> extensions;
	public ExtensionsXml() {
		super();
	}
	public ExtensionsXml(List<ExtensionXml> extensions) {
		super();
		this.extensions = extensions;
	}
	public List<ExtensionXml> getExtensions() {
		return extensions;
	}
	public void setExtensions(List<ExtensionXml> extensions) {
		this.extensions = extensions;
	}
}
