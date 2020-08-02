package com.fimet.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fimet.parser.EFieldGroupXml;

@XmlRootElement(name="fieldGroups")
@XmlAccessorType(XmlAccessType.NONE)
public class FieldGroupsXml {
	@XmlElement(name="group")
	private List<EFieldGroupXml> groups;
	public FieldGroupsXml() {
		super();
	}
	public FieldGroupsXml(List<EFieldGroupXml> groups) {
		super();
		this.groups = groups;
	}
	public List<EFieldGroupXml> getGroups() {
		return groups;
	}
	public void setGroups(List<EFieldGroupXml> groups) {
		this.groups = groups;
	}
}
