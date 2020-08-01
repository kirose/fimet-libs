package com.fimet.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fimet.parser.EFieldGroup;

@XmlRootElement(name="fieldGroups")
@XmlAccessorType(XmlAccessType.NONE)
public class FieldGroupsXml {
	@XmlElement(name="group")
	private List<EFieldGroup> groups;
	public FieldGroupsXml() {
		super();
	}
	public FieldGroupsXml(List<EFieldGroup> groups) {
		super();
		this.groups = groups;
	}
	public List<EFieldGroup> getGroups() {
		return groups;
	}
	public void setGroups(List<EFieldGroup> groups) {
		this.groups = groups;
	}
}
