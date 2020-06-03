package com.fimet.simulator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 *
 */
@XmlRootElement(name="simulator")
@XmlAccessorType(XmlAccessType.NONE)
public class ESimulator implements IESimulator {
	@XmlAttribute(name="name")
	private String name;
	@XmlAttribute(name="parser")
	private String parser;
	@XmlAttribute(name="model")
	private String model;
	@XmlAttribute(name="address")
	private String address;
	@XmlAttribute(name="port")
	private Integer port;
	@XmlAttribute(name="server")
	private boolean server;
	@XmlAttribute(name="adapter")
	private String adapter;
	public ESimulator() {
		super();
	}
	public ESimulator(String name, String model, String parser, String address, Integer port, Boolean server, String adapter) {
		this.name = name;
		this.model = model;
		this.parser = parser;
		this.address = address;
		this.port = port;
		this.server = server;
		this.adapter = adapter;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParser() {
		return parser;
	}
	public void setParser(String parser) {
		this.parser = parser;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public boolean isServer() {
		return server;
	}
	public void setServer(boolean server) {
		this.server = server;
	}
	public String getAdapter() {
		return adapter;
	}
	public void setAdapter(String adapter) {
		this.adapter = adapter;
	}
}
