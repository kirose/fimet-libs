package com.fimet.simulator.field;


import com.fimet.parser.IMessage;
import com.fimet.simulator.ISimulatorField;
import com.fimet.utils.RandomUtils;

public class IfHasSetRandom12N implements ISimulatorField {
	public static final IfHasSetRandom12N INSTANCE = new IfHasSetRandom12N();
	public static IfHasSetRandom12N getInstance() {
		return INSTANCE;
	}
	@Override
	public void simulate(IMessage message, String idField) {
		if (message.hasField(idField)) {
			message.setValue(idField, RandomUtils.randomNumeric(12));
		}
	}
}
