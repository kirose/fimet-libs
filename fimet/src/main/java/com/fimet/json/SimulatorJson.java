package com.fimet.json;


import com.fimet.simulator.ESimulator;
import com.fimet.simulator.IESimulator;

public class SimulatorJson {
	private Integer id;
	private String name;
	private String parser;
	private String model;
	private String address;
	private Integer port;
	private Boolean server;
	private String adapter;
	private SimulatorExtensionJson extension;

	public SimulatorJson() {
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
	public Boolean getServer() {
		return server;
	}
	public void setServer(Boolean server) {
		this.server = server;
	}
	public String getParser() {
		return parser;
	}
	public void setParser(String parser) {
		this.parser = parser;
	}
	public String getAdapter() {
		return adapter;
	}
	public void setAdapter(String adapter) {
		this.adapter = adapter;
	}
	public SimulatorExtensionJson getExtension() {
		return extension;
	}
	public void setExtension(SimulatorExtensionJson extension) {
		this.extension = extension;
	}
	public IESimulator toESimulator() {
		return new ESimulator(name, model, parser, address, port, server, adapter);
	}

}
