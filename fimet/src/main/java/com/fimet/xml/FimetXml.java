package com.fimet.xml;

import java.io.FileOutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "fimet")
@XmlAccessorType(XmlAccessType.NONE)
public class FimetXml {
	@XmlElement(name="source")
	private SourceXml source;
	@XmlElement(name="managers")
	private ManagersXml managers;
	@XmlElement(name="properties")
	private PropertiesXml properties;
	@XmlElement(name="simulators")
	private SimulatorsXml simulators;
	public FimetXml() {}
	public SourceXml getSource() {
		return source;
	}
	public void setSource(SourceXml source) {
		this.source = source;
	}
	public ManagersXml getManagers() {
		return managers;
	}
	public void setManagers(ManagersXml managers) {
		this.managers = managers;
	}
	public SimulatorsXml getSimulators() {
		return simulators;
	}
	public void setSimulators(SimulatorsXml simulators) {
		this.simulators = simulators;
	}
	public PropertiesXml getProperties() {
		return properties;
	}
	public void setProperties(PropertiesXml properties) {
		this.properties = properties;
	}
	public String getPropertyValue(String name) {
		return properties != null ? properties.getPropertyValue(name): null;
	}
	public Integer getPropertyInt(String name) {
		String value = getPropertyValue(name);
		if (value != null) {
			try {
				return Integer.valueOf(value);
			} catch (Exception e) {}
		}
		return null;
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
		fimet.setManagers(new ManagersXml(managers));
		fimet.setSource(new SourceXml("fimet.db"));
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
