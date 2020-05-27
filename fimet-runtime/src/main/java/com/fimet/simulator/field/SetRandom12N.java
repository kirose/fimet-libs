package com.fimet.simulator.field;


import com.fimet.parser.IMessage;
import com.fimet.simulator.ISimulatorField;
import com.fimet.utils.RandomUtils;

public class SetRandom12N implements ISimulatorField {
	public static final SetRandom12N INSTANCE = new SetRandom12N();
	public static SetRandom12N getInstance() {
		return INSTANCE;
	}
	@Override
	public void simulate(IMessage message, String idField) {
		message.setValue(idField, RandomUtils.randomNumeric(12));
	}
}
