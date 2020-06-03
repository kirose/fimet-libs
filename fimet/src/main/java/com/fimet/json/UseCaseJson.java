package com.fimet.json;

import java.util.List;

import com.fimet.utils.JsonUtils;

public class UseCaseJson {
	private String name;
	private String authorization;
	private MessageJson message;
	private String simulatorExtension;
	private List<SimulatorJson> simulators;
	public UseCaseJson() {
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAuthorization() {
		return authorization;
	}
	public void setAuthorization(String authorization) {
		this.authorization = authorization;
	}
	public MessageJson getMessage() {
		return message;
	}
	public void setMessage(MessageJson message) {
		this.message = message;
	}
	public String getSimulatorExtension() {
		return simulatorExtension;
	}
	public void setSimulatorExtension(String simulatorExtension) {
		this.simulatorExtension = simulatorExtension;
	}
	public List<SimulatorJson> getSimulators() {
		return simulators;
	}
	public void setSimulators(List<SimulatorJson> simulators) {
		this.simulators = simulators;
	}
	public String toString() {
		return JsonUtils.toJson(this);
	}
	public long hashCodeExtensions() {
		final int prime = 31;
		long result = 1;
		if (simulators != null && !simulators.isEmpty()) {
			for (SimulatorJson s : simulators) {
				result = prime * result + (s.getExtension() == null ? 0 : s.getExtension().hashCodeExtension());
			}
		}
		return result;
	}
}