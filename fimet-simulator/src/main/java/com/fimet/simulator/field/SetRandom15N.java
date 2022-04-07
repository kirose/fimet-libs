package com.fimet.simulator.field;

import com.fimet.parser.IMessage;
import com.fimet.simulator.ISimulatorField;
import com.fimet.utils.RandomUtils;

public class SetRandom15N implements ISimulatorField {
	public static final SetRandom15N INSTANCE = new SetRandom15N();
	public static SetRandom15N getInstance() {
		return INSTANCE;
	}
	@Override
	public void simulate(IMessage message, String idField) {
		message.setValue(idField, RandomUtils.randomNumeric(15));
	}
}
