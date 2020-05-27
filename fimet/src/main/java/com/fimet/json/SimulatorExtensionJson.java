package com.fimet.json;

import java.util.List;

public class SimulatorExtensionJson {
	private Boolean timeout;
	private List<String> validations;
	private SimulatorRules rules;
	public Boolean getTimeout() {
		return timeout;
	}
	public void setTimeout(Boolean timeout) {
		this.timeout = timeout;
	}
	public List<String> getValidations() {
		return validations;
	}
	public void setValidations(List<String> validations) {
		this.validations = validations;
	}
	public SimulatorRules getRules() {
		return rules;
	}
	public void setRules(SimulatorRules rules) {
		this.rules = rules;
	}
	public long hashInValidations() {
		final int prime = 31;
		long result = 1;
		if (validations != null && !validations.isEmpty()) {
			for (String s : validations) {
				result = prime * result + ((s == null) ? 0 : s.hashCode());
			}
		}
		return result;
	}
	public long hashCodeExtension() {
		final int prime = 31;
		long result = 1;
		result = prime * result + (timeout == null ? 0 : timeout.hashCode());
		result = prime * result + hashInValidations();
		result = prime * result + (rules == null ? 0 : rules.hashCode());
		return result;
	}
}
