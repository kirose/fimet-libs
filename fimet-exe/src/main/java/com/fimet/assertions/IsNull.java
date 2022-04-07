package com.fimet.assertions;

public class IsNull extends AbstractAssertion {
	
	public IsNull(Object value) {
		super();
		this.value = value;
	}
	@Override
	protected boolean doExecute() {
		return value==null;
	}
	public String toString() {
		return status()
				.append("[NULL]")
				.append("V:[").append(value).append("]")
				.append("E:[null]")
				.toString();
	}
	public Object getExpected() {
		return null;
	}
}
