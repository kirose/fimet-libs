package com.fimet.assertions;

public interface IAssertionResult {
	String getType();
	String getName();
	Object getValue();
	Object getExpected();
	Status getStatus();
	boolean isCorrect();
}
