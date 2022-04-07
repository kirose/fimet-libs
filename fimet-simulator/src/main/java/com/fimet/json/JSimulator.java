package com.fimet.json;


import com.fimet.simulator.IESimulator;

public class JSimulator implements IESimulator {
	private Integer id;
	private String name;
	private String parser;
	private String group;
	private String model;
	private String address;
	private Integer port;
	private Boolean server;
	private String adapter;
	private Integer maxConnections;
	private JSimulatorExtension extension;

	public JSimulator() {
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
	public JSimulatorExtension getExtension() {
		return extension;
	}
	public void setExtension(JSimulatorExtension extension) {
		this.extension = extension;
	}
	@Override
	public Boolean isServer() {
		return server;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	@Override
	public String getGroup() {
		return group;
	}
	public Integer getMaxConnections() {
		return maxConnections;
	}
	public void setMaxConnections(Integer maxConnections) {
		this.maxConnections = maxConnections;
	}
}
