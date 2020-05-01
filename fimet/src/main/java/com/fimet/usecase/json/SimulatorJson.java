package com.fimet.usecase.json;


import com.fimet.simulator.PSimulator;

public class SimulatorJson {
	private String id;
	private String parser;
	private String model;
	private String address;
	private Integer port;
	private Boolean server;
	private String adapter;
	private SimulatorExtensionJson extension;

	public SimulatorJson() {
	}
	public String getId() {
		return id;
	}
	public void setId(String name) {
		this.id = name;
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
	public PSimulator toPSimulator() {
		return new PSimulator(id, model, parser, address, port, server, adapter);
	}

}
