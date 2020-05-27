package com.fimet.simulator.field;


import com.fimet.parser.IMessage;
import com.fimet.simulator.ISimulatorField;
import com.fimet.utils.RandomUtils;

public class SetRandom12AN implements ISimulatorField {
	public static final SetRandom12AN INSTANCE = new SetRandom12AN();
	public static SetRandom12AN getInstance() {
		return INSTANCE;
	}
	@Override
	public void simulate(IMessage message, String idField) {
		message.setValue(idField, RandomUtils.randomAlphaNumeric(12));
	}
}
