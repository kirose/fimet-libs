package com.fimet.simulator.field;

import com.fimet.iso8583.parser.Message;
import com.fimet.simulator.AbstractRandom;
import com.fimet.simulator.ISimulatorField;

public class SetRandom15N extends AbstractRandom implements ISimulatorField {
	private static SetRandom15N instance;
	public static SetRandom15N getInstance() {
		if (instance == null) {
			instance = new SetRandom15N();
		}
		return instance;
	}
	@Override
	public void simulate(Message message, String idField) {
		message.setValue(idField, random(15));
	}
}
