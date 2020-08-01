package com.fimet.usecase;

import java.util.List;

public class RSimulator implements IRSimulator {
	private String name;
	private String model;
	private String address;
	private Integer port;
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
	public List<IRAssertion> getAssertions() {
		return assertions;
	}
	public void setAssertions(List<IRAssertion> validations) {
		this.assertions = validations;
	}
}
