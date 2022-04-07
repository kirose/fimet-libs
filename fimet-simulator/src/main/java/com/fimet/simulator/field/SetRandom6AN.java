package com.fimet.simulator.field;


import com.fimet.parser.IMessage;
import com.fimet.simulator.ISimulatorField;
import com.fimet.utils.RandomUtils;

public class SetRandom6AN implements ISimulatorField {
	public static final SetRandom6AN INSTANCE = new SetRandom6AN();
	public static SetRandom6AN getInstance() {
		return INSTANCE;
	}
	@Override
	public void simulate(IMessage message, String idField) {
		message.setValue(idField, RandomUtils.randomAlphaNumeric(6));
	}
}
