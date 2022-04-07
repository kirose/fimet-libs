package com.fimet.simulator.field;

import com.fimet.parser.IMessage;
import com.fimet.simulator.ISimulatorField;

public class IfHasSetRRN implements ISimulatorField {
	public static final IfHasSetRRN INSTANCE = new IfHasSetRRN();
	public static IfHasSetRRN getInstance() {
		return INSTANCE;
	}
	@Override
	public void simulate(IMessage message, String idField) {
		if (!message.hasField(idField)) {
			return;
		}
		String rrn = message.getValue("37");
		if (rrn != null) {
			message.setValue(idField, rrn);
		}
	}
}
