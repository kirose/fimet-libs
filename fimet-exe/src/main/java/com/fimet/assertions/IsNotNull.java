package com.fimet.assertions;

public class IsNotNull extends AbstractAssertion {
	
	public IsNotNull(Object value) {
		super();
		this.value = value;
	}
	@Override
	protected boolean doExecute() {
		return value!=null;
	}
	public String toString() {
		return status()
				.append("[NOTNULL]")
				.append("V:[").append(value).append("]")
				.append("E:[null]")
				.toString();
	}
	public Object getExpected() {
		return null;
	}
}
