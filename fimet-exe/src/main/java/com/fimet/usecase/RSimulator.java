package com.fimet.usecase;

import java.util.List;

public class RSimulator implements IRSimulator {
	private String name;
	private String model;
	private String parser;
	private String address;
	private Integer port;
	private String type;
	private String adapter;
	private String state;
	private List<IRAssertion> assertions;
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
	public String getAdapter() {
		return adapter;
	}
	public void setAdapter(String adapter) {
		this.adapter = adapter;
	}
	public List<IRAssertion> getAssertions() {
		return assertions;
	}
	public void setAssertions(List<IRAssertion> validations) {
		this.assertions = validations;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
