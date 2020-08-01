package com.fimet.simulator;

public class ValidationResult {
	private String validation;
	private boolean success;
	public ValidationResult() {}
	public ValidationResult(String validation, boolean success) {
		super();
		this.validation = validation;
		this.success = success;
	}
	public boolean isFail() {
		return !success;
	}
	public boolean isSuccess() {
		return success;
	}
	public String getValidation() {
		return validation;
	}
	public String toString() {
		return "["+(success ? "SUCCESSFUL" : "FAILURE")+"][" + validation + "]";
	}
}
