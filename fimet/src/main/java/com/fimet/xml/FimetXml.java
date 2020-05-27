package com.fimet.xml;

import java.io.FileOutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;

import com.fimet.simulator.PSimulator;
import com.fimet.sqlite.FimetCreator;

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
	private List<SimulatorXml> simulators;
	private List<SimulatorModelXml> models;
	private List<ExtensionXml> extensions;
	private List<ParserXml> parsers;
	private List<FieldGroupXml> fieldGroups;
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
	public List<SimulatorXml> getSimulators() {
		return simulators;
	}
	public void setSimulators(List<SimulatorXml> simulators) {
		this.simulators = simulators;
	}
	public List<PSimulator> getPSimulators(){
		List<PSimulator> list = new ArrayList<>();
		if (simulators != null && !simulators.isEmpty()) {
			for (SimulatorXml s : simulators) {
				list.add(s.toPSimulator());
			} 
		}
		return list;
	}
	@XmlElementWrapper(name="simulatorModels")
	@XmlElement(name = "simulatorModel")
	public List<SimulatorModelXml> getModels() {
		return models;
	}
	public void setModels(List<SimulatorModelXml> models) {
		this.models = models;
	}
	@XmlElementWrapper(name = "properties")
	@XmlElement(name="property")
	public List<PropertyXml> getProperties() {
		return properties;
	}
	public void setProperties(List<PropertyXml> properties) {
		this.properties = properties;
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
	public Map<String, String> getExtensionsMap(){
		Map<String,String> map = new HashMap<String, String>();
		if (extensions != null && !extensions.isEmpty()) {
			for (ExtensionXml p : extensions) {
				map.put(p.getId(), p.getClassName());
			}
		}
		return map;
	}
	@XmlElementWrapper(name = "parsers")
	@XmlElement(name="parser")
	public List<ParserXml> getParsers() {
		return parsers;
	}
	public void setParsers(List<ParserXml> parsers) {
		this.parsers = parsers;
	}
	@XmlElementWrapper(name = "fieldGroups")
	@XmlElement(name="fieldGroup")
	public List<FieldGroupXml> getFieldGroups() {
		return fieldGroups;
	}
	public void setFieldGroups(List<FieldGroupXml> fieldGroups) {
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
	public static void main(String[] args) throws Exception {
		read();
		write();
	}
	private static void read() throws Exception {
		//File xmlFile = new File("C:\\eclipse\\wsfimetapp\\fimet-core\\src\\main\\resources\\fimet.xml");
		//InputStream stream = Manager.class.getClass().getResourceAsStream("fimet.xml");
		java.io.File xmlFile = new java.io.File("C:\\eclipse\\wsfimetlibs\\fimet-test\\resources\\fimet.xml");
		JAXBContext jaxbContext = JAXBContext.newInstance(FimetXml.class);              
	    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
	    FimetXml fimet = (FimetXml) jaxbUnmarshaller.unmarshal(xmlFile);
	    //FimetXml fimet = (FimetXml) jaxbUnmarshaller.unmarshal(stream);
	    System.out.println("xml parsed:");
	    System.out.println(fimet);
	}
	private static void write() throws Exception {
		FimetXml fimet = new FimetXml();
		List<ManagerXml> managers = new ArrayList<>();
		managers.add(new ManagerXml("MessengerManager","com.fimet.net.MessengerManager"));
		managers.add(new ManagerXml("ParserManager","com.fimet.iso8583.ParserManager"));
		fimet.setManagers(managers);
		List<DatabaseXml> databases = new ArrayList<>();
		DatabaseXml database = new DatabaseXml();
		database.setId("fimet");
		database.setPath("resources/fimet.db");
		database.setCreator(FimetCreator.class.getName());
		databases.add(database);
		fimet.setDatabases(databases);
		JAXBContext context = JAXBContext.newInstance(FimetXml.class);  
	    Marshaller marshaller = context.createMarshaller();  
	    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);  
	    marshaller.marshal(fimet, new FileOutputStream("C:\\eclipse\\wsfimetapp\\fimet-core\\src\\main\\resources\\fimet2.xml"));
	}
	public String toString() {
		try {
			JAXBContext context = JAXBContext.newInstance(FimetXml.class);  
		    Marshaller marshaller = context.createMarshaller();  
		    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		    StringWriter sw = new StringWriter();
		    marshaller.marshal(this, sw);
		    return sw.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
