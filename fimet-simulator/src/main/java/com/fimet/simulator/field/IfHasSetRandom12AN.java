package com.fimet.simulator.field;


import com.fimet.iso8583.parser.Message;
import com.fimet.simulator.AbstractRandom;
import com.fimet.simulator.ISimulatorField;

public class IfHasSetRandom12AN extends AbstractRandom implements ISimulatorField {
	private static IfHasSetRandom12AN instance;
	public static IfHasSetRandom12AN getInstance() {
		if (instance == null) {
			instance = new IfHasSetRandom12AN();
		}
		return instance;
	}
	@Override
	public void simulate(Message message, String idField) {
		if (message.hasField(idField)) {
			message.setValue(idField, randomAlfaNumeric(12));
		}
	}
}
