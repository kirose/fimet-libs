package com.fimet.simulator.field;


import com.fimet.parser.IMessage;
import com.fimet.simulator.ISimulatorField;
import com.fimet.utils.RandomUtils;

public class IfHasSetRandom12AN implements ISimulatorField {
	public static final IfHasSetRandom12AN INSTANCE = new IfHasSetRandom12AN();
	public static IfHasSetRandom12AN getInstance() {
		return INSTANCE;
	}
	@Override
	public void simulate(IMessage message, String idField) {
		if (message.hasField(idField)) {
			message.setValue(idField, RandomUtils.randomAlphaNumeric(12));
		}
	}
}
