package com.fimet.assertions;

public class NotEquals extends AbstractAssertion {
	
	private Object expected;
	
	public NotEquals(Object expected, Object value) {
		super();
		this.value = value;
		this.expected = expected;
	}
	@Override
	protected boolean doExecute() {
		return AssertionUtils.notEquals(value, expected);
	}
	public String toString() {
		return status()
				.append("[NOTEQ]")
				.append("V:[").append(value).append("]")
				.append("E:[").append(expected).append("]")
				.toString();
	}
	public Object getExpected() {
		return expected;
	}
}
