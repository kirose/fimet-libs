package com.fimet.simulator.field;


import com.fimet.iso8583.parser.Message;
import com.fimet.simulator.AbstractRandom;
import com.fimet.simulator.ISimulatorField;

public class SetRandom6AN extends AbstractRandom implements ISimulatorField {
	private static SetRandom6AN instance;
	public static SetRandom6AN getInstance() {
		if (instance == null) {
			instance = new SetRandom6AN();
		}
		return instance;
	}
	@Override
	public void simulate(Message message, String idField) {
		message.setValue(idField, randomAlfaNumeric(6));
	}
}
