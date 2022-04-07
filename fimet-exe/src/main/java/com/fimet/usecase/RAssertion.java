package com.fimet.usecase;

import com.fimet.assertions.IAssertionResult;

public class RAssertion implements IRAssertion {
	private String name;
	private String type;
	private String status;
	private String value;
	private String expected;
	public RAssertion(IAssertionResult a) {
		name = a.getName();
		type = a.getType();
		status = a.getStatus().toString();
		value = a.getValue()!=null?a.getValue().toString():null;
		expected = a.getExpected()!=null?a.getExpected().toString():null;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public boolean isCorrect() {
		return com.fimet.assertions.Status.SUCCESS.toString().equals(status);
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getExpected() {
		return expected;
	}
	public void setExpected(String expected) {
		this.expected = expected;
	}
}
