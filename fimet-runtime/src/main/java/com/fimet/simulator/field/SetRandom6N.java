package com.fimet.simulator.field;


import com.fimet.parser.IMessage;
import com.fimet.simulator.ISimulatorField;
import com.fimet.utils.RandomUtils;

public class SetRandom6N implements ISimulatorField {
	public static final SetRandom6N INSTANCE = new SetRandom6N();
	public static SetRandom6N getInstance() {
		return INSTANCE;
	}
	@Override
	public void simulate(IMessage message, String idField) {
		message.setValue(idField, RandomUtils.randomNumeric(6));
	}
}
