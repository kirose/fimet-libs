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
@XmlRootElement(name="message")
@XmlAccessorType(XmlAccessType.NONE)
public class ESimulatorMessageXml implements IESimulatorMessage {
	@XmlAttribute(name="header")
	private String header;
	@XmlAttribute(name="mti")
	private String mti;
	@XmlAttribute(name="type")
	private String type;
	@XmlElement(name = "del")
	private List<String> delFields;
	@XmlElement(name = "add")
	private List<SimulatorField> addFields;
	public ESimulatorMessageXml() {
		super();
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public String getMti() {
		return mti;
	}
	public void setMti(String mti) {
		this.mti = mti;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<String> getDelFields() {
		return delFields;
	}
	public void setDelFields(List<String> delFields) {
		this.delFields = delFields;
	}
	public List<SimulatorField> getAddFields() {
		return addFields;
	}
	public void setAddFields(List<SimulatorField> addFields) {
		this.addFields = addFields;
	}
	public ESimulatorMessageXml addField(String key, Class<?> clazz) {
		addFields.add(new SimulatorField(key, clazz));
		return this;
	}
	public ESimulatorMessageXml addField(String key, String value) {
		addFields.add(new SimulatorField(key, value));
		return this;
	}
}
