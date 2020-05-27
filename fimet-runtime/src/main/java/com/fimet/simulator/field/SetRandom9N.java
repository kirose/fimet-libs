package com.fimet.simulator.field;

import com.fimet.parser.IMessage;
import com.fimet.simulator.ISimulatorField;
import com.fimet.utils.RandomUtils;

public class SetRandom9N implements ISimulatorField {
	public static final SetRandom9N INSTANCE = new SetRandom9N();
	public static SetRandom9N getInstance() {
		return INSTANCE;
	}
	@Override
	public void simulate(IMessage message, String idField) {
		message.setValue(idField, RandomUtils.randomNumeric(9));
	}
}
