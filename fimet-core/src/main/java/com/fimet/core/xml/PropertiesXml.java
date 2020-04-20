package com.fimet.core.xml;


import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="properties")
@XmlAccessorType(XmlAccessType.NONE)
public class PropertiesXml {
	@XmlElement(name="property")
	private List<PropertyXml> properties;
	public PropertiesXml() {
		super();
	}
	public PropertiesXml(List<PropertyXml> properties) {
		super();
		this.properties = properties;
	}
	public List<PropertyXml> getProperties() {
		return properties;
	}
	public void setProperties(List<PropertyXml> properties) {
		this.properties = properties;
	}
	public String getPropertyValue(String name) {
		if (properties != null && !properties.isEmpty()) {
			for (PropertyXml p : properties) {
				if (p.getName().equals(name)) {
					return p.getValue();
				}
			}
		}
		return null;
	}
}
