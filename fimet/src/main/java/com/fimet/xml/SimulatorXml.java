package com.fimet.xml;



import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.fimet.simulator.PSimulator;

@XmlRootElement(name="simulator")
@XmlAccessorType(XmlAccessType.NONE)
public class SimulatorXml {
	@XmlAttribute(name="id")
	private String id;
	@XmlAttribute(name="model")
	private String model;
	@XmlAttribute(name="parser")
	private String parser;
	@XmlAttribute(name="address")
	private String address;
	@XmlAttribute(name="port")
	private Integer port;
	@XmlAttribute(name="server")
	private boolean server;
	@XmlAttribute(name="adapter")
	private String adapter;
	public SimulatorXml() {
		super();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getParser() {
		return parser;
	}
	public void setParser(String parser) {
		this.parser = parser;
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
	public void setServer(boolean isServer) {
		this.server = isServer;
	}
	public String getAdapter() {
		return adapter;
	}
	public void setAdapter(String adapter) {
		this.adapter = adapter;
	}
	public PSimulator toPSimulator() {
		return new PSimulator(id, model, parser, address, port, server, adapter); 
	}
}
