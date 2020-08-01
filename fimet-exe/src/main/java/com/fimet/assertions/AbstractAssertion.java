package com.fimet.assertions;

import com.fimet.assertions.IAssertion;
import com.fimet.assertions.IAssertionResult;

public abstract class AbstractAssertion implements IAssertion, IAssertionResult {
	protected Object value;
	protected String type = getClass().getSimpleName();
	protected String name;
	protected Status status = Status.NOT_EXECUTED;
	public AbstractAssertion() {
	}
	public IAssertionResult execute(String name) {
		this.name = name;
		boolean success = doExecute();
		this.status = success ? Status.SUCCESS : Status.FAIL;
		return this;
	}
	protected StringBuilder status() {
		StringBuilder s = new StringBuilder();
		s.append("[").append(status).append("]");
		return s;
	}
	abstract protected boolean doExecute();
		
	public Status getStatus() {
		return status;
	}
	public String getType() {
		return type;
	}
	public Object getValue() {
		return value;
	}
	abstract public Object getExpected();
	public boolean isCorrect() {
		return status == Status.SUCCESS;
	}
	public String getName() {
		return name;
	}
}
