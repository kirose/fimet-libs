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
public class ESimulatorXml implements IESimulator {
	@XmlAttribute(name="name")
	private String name;
	@XmlAttribute(name="parser")
	private String parser;
	@XmlAttribute(name="model")
	private String model;
	@XmlAttribute(name="socket")
	private String socket;
	@XmlAttribute(name="address")
	private String address;
	@XmlAttribute(name="port")
	private Integer port;
	@XmlAttribute(name="server")
	private Boolean server;
	@XmlAttribute(name="adapter")
	private String adapter;
	@XmlAttribute(name="group")
	private String group;
	@XmlAttribute(name="maxConnections")
	private Integer maxConnections;
	public ESimulatorXml() {
		super();
	}
	public ESimulatorXml(String name, String model, String parser, String address, Integer port, Boolean server, String adapter) {
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
	public Boolean isServer() {
		return server;
	}
	public void setServer(Boolean server) {
		this.server = server;
	}
	public String getAdapter() {
		return adapter;
	}
	public void setAdapter(String adapter) {
		this.adapter = adapter;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getSocket() {
		return socket;
	}
	public void setSocket(String socket) {
		this.socket = socket;
	}
	public Integer getMaxConnections() {
		return maxConnections;
	}
	public void setMaxConnections(Integer maxConnections) {
		this.maxConnections = maxConnections;
	}
}
