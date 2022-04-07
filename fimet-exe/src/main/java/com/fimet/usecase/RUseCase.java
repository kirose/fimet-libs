package com.fimet.usecase;

import java.util.List;

public class RUseCase implements IRUseCase {
	private String name;
	private String acquirer;
	private String responseCode;
	private String authorizationCode;
	private String status;
	private String statusAssertions;
	private Long startTime;
	private Long executionTime;
	private List<IRSimulator> simulators;
	public RUseCase() {
	}
	public RUseCase(String name) {
		super();
		this.name = name;
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
	public String getStatusAssertions() {
		return statusAssertions;
	}
	public void setStatusAssertions(String statusAssertions) {
		this.statusAssertions = statusAssertions;
	}
}
