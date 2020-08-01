package com.fimet.assertions;

public class IsTrue extends AbstractAssertion {
	
	
	public IsTrue(boolean value) {
		super();
		this.value = value;
	}
	@Override
	protected boolean doExecute() {
		return (Boolean)value;
	}
	public String toString() {
		return status()
				.append("[ISTRUE]")
				.append("V:[").append(value).append("]")
				.append("E:[true]")
				.toString();
	}
	public Object getExpected() {
		return Boolean.TRUE;
	}
}
