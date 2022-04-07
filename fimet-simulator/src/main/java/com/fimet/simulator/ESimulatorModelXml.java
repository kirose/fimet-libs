package com.fimet.simulator;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 *
 */
@XmlRootElement(name="model")
@XmlAccessorType(XmlAccessType.NONE)
public class ESimulatorModelXml implements IESimulatorModel {
	@XmlAttribute(name="name")
	private String name;
	@XmlAttribute(name="class")
	private String classModel;
	@XmlAttribute(name="path")
	private String path;
	@XmlElement(name = "message")
	private List<ESimulatorMessageXml> messages;
	public ESimulatorModelXml() {
		super();
	}
	public ESimulatorModelXml(String name, String className) {
		super();
		this.name = name;
		this.classModel = className;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return name;
	}
	@Override
	public String getClassModel() {
		return classModel;
	}
	@Override
	public void setClassModel(String className) {
		this.classModel = className;
	}
	public List<ESimulatorMessageXml> getMessages() {
		return messages;
	}
	public void setMessages(List<ESimulatorMessageXml> messages) {
		this.messages = messages;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
}
