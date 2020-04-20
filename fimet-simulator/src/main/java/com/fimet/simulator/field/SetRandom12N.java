package com.fimet.simulator.field;


import com.fimet.core.iso8583.parser.Message;
import com.fimet.simulator.msg.AbstractRandom;
import com.fimet.simulator.msg.ISimulatorField;

public class SetRandom12N extends AbstractRandom implements ISimulatorField {
	private static SetRandom12N instance;
	public static SetRandom12N getInstance() {
		if (instance == null) {
			instance = new SetRandom12N();
		}
		return instance;
	}
	@Override
	public void simulate(Message message, String idField) {
		message.setValue(idField, random(12));
	}
}
