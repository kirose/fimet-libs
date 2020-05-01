package com.fimet.simulator.field;


import com.fimet.iso8583.parser.Message;
import com.fimet.simulator.msg.AbstractRandom;
import com.fimet.simulator.msg.ISimulatorField;

public class SetRandom6N extends AbstractRandom implements ISimulatorField {
	private static SetRandom6N instance;
	public static SetRandom6N getInstance() {
		if (instance == null) {
			instance = new SetRandom6N();
		}
		return instance;
	}
	@Override
	public void simulate(Message message, String idField) {
		message.setValue(idField, random(6));
	}
}
