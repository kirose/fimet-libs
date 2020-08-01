package com.fimet.simulator.field;


import com.fimet.parser.IMessage;
import com.fimet.simulator.ISimulatorField;
import com.fimet.utils.RandomUtils;

public class IfHasSetRandom6AN implements ISimulatorField {
	public static final IfHasSetRandom6AN INSTANCE = new IfHasSetRandom6AN();
	public static IfHasSetRandom6AN getInstance() {
		return INSTANCE;
	}
	@Override
	public void simulate(IMessage message, String idField) {
		if (message.hasField(idField)) {
			message.setValue(idField, RandomUtils.randomAlphaNumeric(6));
		}
	}
}
