package com.fimet.usecase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RUseCase implements IRUseCase {
	private String name;
	private String acquirer;
	private String responseCode;
	private String authorizationCode;
	private String status;
	private Long startTime;
	private Long executionTime;
	private Map<String, String> properties;
	private List<IRSimulator> simulators;
	public RUseCase() {
		properties = new HashMap<>();
	}
	public RUseCase(String name) {
		super();
		this.name = name;
		properties = new HashMap<>();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getStartTime() {
		return startTime;
	}
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
	public Long getExecutionTime() {
		return executionTime;
	}
	public void setExecutionTime(Long executionTime) {
		this.executionTime = executionTime;
	}
	public Map<String, String> getProperties() {
		return properties;
	}
	public void setProperties(Map<String, String> data) {
		this.properties = data;
	}
	public List<IRSimulator> getSimulators() {
		return simulators;
	}
	public void setSimulators(List<IRSimulator> simulators) {
		this.simulators = simulators;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public String getAuthorizationCode() {
		return authorizationCode;
	}
	public boolean hasProperty(String key) {
		return properties.containsKey(key);
	}
	public String getProperty(String key) {
		return properties.get(key);
	}
	public void setProperty(String key, String value) {
		properties.put(key, value);
	}
	public String getAcquirer() {
		return acquirer;
	}
	public void setAcquirer(String acquirer) {
		this.acquirer = acquirer;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public void setAuthorizationCode(String authorizationCode) {
		this.authorizationCode = authorizationCode;
	}
}
