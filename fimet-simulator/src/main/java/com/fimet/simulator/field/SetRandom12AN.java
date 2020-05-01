package com.fimet.simulator.field;


import com.fimet.iso8583.parser.Message;
import com.fimet.simulator.msg.AbstractRandom;
import com.fimet.simulator.msg.ISimulatorField;

public class SetRandom12AN extends AbstractRandom implements ISimulatorField {
	private static SetRandom12AN instance;
	public static SetRandom12AN getInstance() {
		if (instance == null) {
			instance = new SetRandom12AN();
		}
		return instance;
	}
	@Override
	public void simulate(Message message, String idField) {
		message.setValue(idField, randomAlfaNumeric(12));
	}
}
