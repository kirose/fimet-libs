package com.fimet.sqlite.simulator;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.fimet.simulator.IESimulator;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 *
 */
@XmlRootElement(name="simulator")
@DatabaseTable(tableName = "Simulator")
public class ESimulator implements IESimulator {
	@DatabaseField(id = true)
	private Integer id;
	@XmlAttribute(name="name")
	@DatabaseField(canBeNull = false)
	private String name;
	@XmlAttribute(name="parser")
	@DatabaseField(canBeNull = false)
	private String parser;
	@XmlAttribute(name="model")
	@DatabaseField(canBeNull = false)
	private String model;
	@XmlAttribute(name="address")
	@DatabaseField(canBeNull = false)
	private String address;
	@XmlAttribute(name="port")
	@DatabaseField(canBeNull = false)
	private Integer port;
	@XmlAttribute(name="server")
	@DatabaseField(canBeNull = false)
	private boolean server;
	@XmlAttribute(name="adapter")
	@DatabaseField(canBeNull = false)
	private String adapter;
	public ESimulator() {
		super();
	}
	public ESimulator(String name, String model, String parser, String address, Integer port, Boolean server, String adapter) {
		this(null, name, model, parser, address, port, server, adapter);
	}
	public ESimulator(Integer id, String name, String model, String parser, String address, Integer port, Boolean server, String adapter) {
		this.id = id;
		this.name = name;
		this.model = model;
		this.parser = parser;
		this.address = address;
		this.port = port;
		this.server = server;
		this.adapter = adapter;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
