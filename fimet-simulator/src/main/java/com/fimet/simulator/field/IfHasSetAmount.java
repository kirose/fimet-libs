package com.fimet.simulator.field;

import com.fimet.parser.IMessage;
import com.fimet.simulator.ISimulatorField;

public class IfHasSetAmount implements ISimulatorField {
	public static final IfHasSetAmount INSTANCE = new IfHasSetAmount();
	public static IfHasSetAmount getInstance() {
		return INSTANCE;
	}
	@Override
	public void simulate(IMessage message, String idField) {
		if (!message.hasField(idField)) {
			return;
		}
		String amount = message.getValue("4");
		if (amount != null) {
			message.setValue(idField, amount);
		}
	}
}
