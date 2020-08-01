package com.fimet.usecase;

public interface IRAssertion {
	public String getName();
	public String getType();
	public String getStatus();
	public boolean isCorrect();
	public String getValue();
	public String getExpected();
}
