package com.fimet.xml;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;

import com.fimet.FimetException;
import com.fimet.FimetLogger;
import com.fimet.parser.EFieldGroup;
import com.fimet.parser.EParser;
import com.fimet.simulator.ESimulator;
import com.fimet.simulator.ESimulatorModel;
import com.fimet.utils.XmlUtils;

@XmlRootElement(name = "fimet")
@XmlAccessorType(XmlAccessType.NONE)
public class FimetXml {
	@XmlAttribute(name="local")
	private String local;
	@XmlAttribute(name="URI")
	private String URI;
	@XmlAttribute(name="xmlns")
	private String xmlns;
	@XmlAttribute(name="xmlns:xsi")
	private String xmlnsXsi;
	@XmlAttribute(name="xsi:schemaLocation")
	private String schemaLocation;
	private Map<QName,Object> attrs;
	private List<DatabaseXml> databases;
	private List<ManagerXml> managers;
	private List<PropertyXml> properties;
	private List<ESimulator> simulators;
	private List<ESimulatorModel> simulatorModels;
	private List<ExtensionXml> extensions;
	private List<EParser> parsers;
	private List<EFieldGroup> fieldGroups;
	public FimetXml() {}
	@XmlAnyAttribute
	public Map<QName,Object> getAny() {
	  return attrs;
	}
	@XmlElementWrapper(name = "databases")
	@XmlElement(name = "database")
	public List<DatabaseXml> getDatabases() {
		return databases;
	}
	public void setDatabases(List<DatabaseXml> databases) {
		this.databases = databases;
	}
	@XmlElementWrapper(name = "properties")
	@XmlElement(name="property")
	public List<PropertyXml> getProperties() {
		return properties;
	}
	public void setProperties(List<PropertyXml> properties) {
		this.properties = properties;
	}
	@XmlElementWrapper(name = "managers")
	@XmlElement(name = "manager")
	public List<ManagerXml> getManagers() {
		return managers;
	}
	public void setManagers(List<ManagerXml> managers) {
		this.managers = managers;
	}
	@XmlElementWrapper(name="simulators")
	@XmlElement(name = "simulator")
	public List<ESimulator> getSimulators() {
		return simulators;
	}
	public void setSimulators(List<ESimulator> simulators) {
		this.simulators = simulators;
	}
	@XmlElementWrapper(name="simulatorModels")
	@XmlElement(name = "model")
	public List<ESimulatorModel> getSimulatorModels() {
		return simulatorModels;
	}
	public void setSimulatorModels(List<ESimulatorModel> models) {
		this.simulatorModels = models;
	}
	public Map<String, String> getPropertiesMap(){
		Map<String,String> map = new HashMap<String, String>();
		if (properties != null && !properties.isEmpty()) {
			for (PropertyXml p : properties) {
				map.put(p.getName(), p.getValue());
			}
		}
		return map;
	}
	@XmlElementWrapper(name = "extensions")
	@XmlElement(name="extension")
	public List<ExtensionXml> getExtensions() {
		return extensions;
	}
	public void setExtensions(List<ExtensionXml> extensions) {
		this.extensions = extensions;
	}
	public Map<Class<?>, String> getExtensionsMap(){
		Map<Class<?>,String> map = new HashMap<Class<?>, String>();
		if (extensions != null && !extensions.isEmpty()) {
			for (ExtensionXml p : extensions) {
				try {
					Class<?> clazz = Class.forName(p.getName());
					map.put(clazz, p.getClassName());
				} catch (ClassNotFoundException e) {
					FimetLogger.error(getClass(), e);
					throw new FimetException("Unknow extension name "+p.getName(), e);
				}
			}
		}
		return map;
	}
	@XmlElementWrapper(name = "parsers")
	@XmlElement(name="parser")
	public List<EParser> getParsers() {
		return parsers;
	}
	public void setParsers(List<EParser> parsers) {
		this.parsers = parsers;
	}
	@XmlElementWrapper(name = "fieldGroups")
	@XmlElement(name="group")
	public List<EFieldGroup> getFieldGroups() {
		return fieldGroups;
	}
	public void setFieldGroups(List<EFieldGroup> fieldGroups) {
		this.fieldGroups = fieldGroups;
	}
	public String getXmlns() {
		return xmlns;
	}
	public void setXmlns(String xmlns) {
		this.xmlns = xmlns;
	}
	public String getXmlnsXsi() {
		return xmlnsXsi;
	}
	public void setXmlnsXsi(String xmlnsXsi) {
		this.xmlnsXsi = xmlnsXsi;
	}
	public String getSchemaLocation() {
		return schemaLocation;
	}
	public void setSchemaLocation(String schemaLocation) {
		this.schemaLocation = schemaLocation;
	}
	public Map<QName, Object> getAttrs() {
		return attrs;
	}
	public String toString() {
		return XmlUtils.toXml(this);
	}
}
