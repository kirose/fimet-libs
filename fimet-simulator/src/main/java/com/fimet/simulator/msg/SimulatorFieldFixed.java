package com.fimet.simulator.msg;

import com.fimet.core.iso8583.parser.Message;

public class SimulatorFieldFixed extends SimulatorField {
	private String idField;
	private String value;
	public SimulatorFieldFixed(String idField, String value) {
		super();
		this.idField = idField;
		this.value = value;
	}
	public void simulate(Message message) {
		message.setValue(idField, value);
	}
}
