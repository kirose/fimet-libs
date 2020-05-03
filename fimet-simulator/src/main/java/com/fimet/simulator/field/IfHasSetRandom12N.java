package com.fimet.simulator.field;


import com.fimet.iso8583.parser.Message;
import com.fimet.simulator.AbstractRandom;
import com.fimet.simulator.ISimulatorField;

public class IfHasSetRandom12N extends AbstractRandom implements ISimulatorField {
	private static IfHasSetRandom12N instance;
	public static IfHasSetRandom12N getInstance() {
		if (instance == null) {
			instance = new IfHasSetRandom12N();
		}
		return instance;
	}
	@Override
	public void simulate(Message message, String idField) {
		if (message.hasField(idField)) {
			message.setValue(idField, random(12));
		}
	}
}
