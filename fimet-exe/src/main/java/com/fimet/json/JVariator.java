package com.fimet.json;

import java.util.List;

public class JVariator {
	private int start;
	private int end;
	private List<JField> fields;
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	public List<JField> getFields() {
		return fields;
	}
	public void setFields(List<JField> fields) {
		this.fields = fields;
	}
}
