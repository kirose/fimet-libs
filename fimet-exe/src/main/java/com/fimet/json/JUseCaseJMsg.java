package com.fimet.json;

import java.util.ArrayList;
import java.util.List;


public class JUseCaseJMsg {
	private String name;
	private String authorization;
	private JMessageMap message;
	private String simulatorExtension;
	private List<JSimulator> simulators;
	public JUseCaseJMsg() {
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
	public JMessageMap getMessage() {
		return message;
	}
	public void setMessage(JMessageMap message) {
		this.message = message;
	}
	public String getSimulatorExtension() {
		return simulatorExtension;
	}
	public void setSimulatorExtension(String simulatorExtension) {
		this.simulatorExtension = simulatorExtension;
	}
	public List<JSimulator> getSimulators() {
		return simulators;
	}
	public void setSimulators(List<JSimulator> simulators) {
		this.simulators = simulators;
	}
	public void addSimulators(JSimulator simulator) {
		if (simulators==null)
			simulators = new ArrayList<>();
		this.simulators.add(simulator);
	}
	public void setAcquirer(JSimulator acquirer) {
		if (simulators==null)
			simulators = new ArrayList<>();
		simulators.set(0, acquirer);
	}
	public JSimulator getAcquirer() {
		return simulators!=null&&!simulators.isEmpty()?simulators.get(0) : null;
	}
	public String toString() {
		return JExeAdapterFactory.toPrettyJson(this);
	}
	public long hashCodeExtensions() {
		final int prime = 31;
		long result = 1;
		if (simulators != null && !simulators.isEmpty()) {
			for (JSimulator s : simulators) {
				result = prime * result + (s.getExtension() == null ? 0 : s.getExtension().hashCodeExtension());
			}
		}
		return result;
	}
}