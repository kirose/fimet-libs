package com.fimet.simulator.field;


import com.fimet.iso8583.parser.Message;
import com.fimet.simulator.msg.AbstractRandom;
import com.fimet.simulator.msg.ISimulatorField;

public class IfHasSetRandom6N extends AbstractRandom implements ISimulatorField {
	private static IfHasSetRandom6N instance;
	public static IfHasSetRandom6N getInstance() {
		if (instance == null) {
			instance = new IfHasSetRandom6N();
		}
		return instance;
	}
	@Override
	public void simulate(Message message, String idField) {
		if (message.hasField(idField)) {
			message.setValue(idField, random(6));
		}
	}
}
