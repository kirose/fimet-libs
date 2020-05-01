package com.fimet.simulator.field;

import com.fimet.iso8583.parser.Message;
import com.fimet.simulator.msg.AbstractRandom;
import com.fimet.simulator.msg.ISimulatorField;

public class SetRandom9N extends AbstractRandom implements ISimulatorField {
	private static SetRandom9N instance;
	public static SetRandom9N getInstance() {
		if (instance == null) {
			instance = new SetRandom9N();
		}
		return instance;
	}
	@Override
	public void simulate(Message message, String idField) {
		message.setValue(idField, random(9));
	}
}
