package com.fimet.assertions;

public interface IAssertion {
	String getType();
	Object getValue();
	Object getExpected();
	Status getStatus();
	IAssertionResult execute(String name);
}
