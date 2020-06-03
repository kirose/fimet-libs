package com.fimet.sqlite.usecase;

import com.fimet.simulator.ValidationResult;

public class Simulator {
	private String model;
	private String address;
	private Integer port;
	private ValidationResult[] validations;
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
	public ValidationResult[] getValidations() {
		return validations;
	}
	public void setValidations(ValidationResult[] validations) {
		this.validations = validations;
	}
}
