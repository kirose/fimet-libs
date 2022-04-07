package com.fimet.assertions;

public class Equals extends AbstractAssertion {
	
	private Object expected;
	
	public Equals(Object expected, Object value) {
		super();
		this.value = value;
		this.expected = expected;
	}
	@Override
	protected boolean doExecute() {
		return AssertionUtils.equals(value, expected);
	}
	public String toString() {
		return status()
				.append("[EQ]")
				.append("V:[").append(value).append("]")
				.append("E:[").append(expected).append("]")
				.toString();
	}
	public Object getExpected() {
		return expected;
	}
}
