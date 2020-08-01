package com.fimet.json;

import java.util.List;
import java.util.Map;

public class JSimulatorExtension {
	private Boolean timeout;
	private List<String> del;
	private Map<String,String> add;
	private Map<String,String> validations;
	public Boolean getTimeout() {
		return timeout;
	}
	public void setTimeout(Boolean timeout) {
		this.timeout = timeout;
	}
	public Map<String,String> getValidations() {
		return validations;
	}
	public void setValidations(Map<String,String> validations) {
		this.validations = validations;
	}
	public List<String> getDel() {
		return del;
	}
	public void setDel(List<String> del) {
		this.del = del;
	}
	public Map<String, String> getAdd() {
		return add;
	}
	public void setAdd(Map<String, String> add) {
		this.add = add;
	}
	public long hashInValidations() {
		final int prime = 31;
		long result = 1;
		if (validations != null && !validations.isEmpty()) {
			for (Map.Entry<String,String> e : validations.entrySet()) {
				result = prime * result + ((e.getKey() == null) ? 0 : e.getKey().hashCode());
				result = prime * result + ((e.getValue() == null) ? 0 : e.getValue().hashCode());
			}
		}
		return result;
	}
	public long hashCodeExtension() {
		final int prime = 31;
		long result = 1;
		result = prime * result + (timeout == null ? 0 : timeout.hashCode());
		result = prime * result + hashInValidations();
		result = prime * result + ((add == null) ? 0 : add.hashCode());
		result = prime * result + ((del == null) ? 0 : del.hashCode());
		return result;
	}
}
