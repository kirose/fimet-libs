package com.fimet.simulator.field;

import com.fimet.parser.IMessage;
import com.fimet.simulator.ISimulatorField;

public class IfHasSetEntryMode implements ISimulatorField {
	public static final IfHasSetEntryMode INSTANCE = new IfHasSetEntryMode();
	public static IfHasSetEntryMode getInstance() {
		return INSTANCE;
	}
	@Override
	public void simulate(IMessage message, String idField) {
		if (!message.hasField(idField)) {
			return;
		}
		String pem = message.getValue("22");
		if (pem != null && pem.length() >= 2) {
			message.setValue(idField, pem.substring(0,2));
		}
	}
}
