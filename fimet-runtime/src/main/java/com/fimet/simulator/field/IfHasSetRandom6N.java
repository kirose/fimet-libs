package com.fimet.simulator.field;


import com.fimet.parser.IMessage;
import com.fimet.simulator.ISimulatorField;
import com.fimet.utils.RandomUtils;

public class IfHasSetRandom6N implements ISimulatorField {
	public static final IfHasSetRandom6N INSTANCE = new IfHasSetRandom6N();
	public static IfHasSetRandom6N getInstance() {
		return INSTANCE;
	}
	@Override
	public void simulate(IMessage message, String idField) {
		if (message.hasField(idField)) {
			message.setValue(idField, RandomUtils.randomNumeric(6));
		}
	}
}
