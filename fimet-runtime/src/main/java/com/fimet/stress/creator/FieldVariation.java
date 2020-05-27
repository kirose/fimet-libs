package com.fimet.stress.creator;

public class FieldVariation {
	String idField;
	int index;
	String value;
	FieldVariation(){}
	public String toString() {
		return idField+":"+value;
	}
	public String getIdField() {
		return idField;
	}
	public String getValue() {
		return value;
	}
}