package com.fimet.simulator.field;

import com.fimet.core.iso8583.parser.Message;
import com.fimet.simulator.msg.ISimulatorField;

public class IfHasSetPanLast4Digits implements ISimulatorField {
	private static IfHasSetPanLast4Digits instance;
	public static IfHasSetPanLast4Digits getInstance() {
		if (instance == null) {
			instance = new IfHasSetPanLast4Digits();
		}
		return instance;
	}
	@Override
	public void simulate(Message message, String idField) {
		if (!message.hasField(idField)) {
			return;
		}
		String pan = message.getPan();
		if (pan != null && pan.length() >= 4) {
			message.setValue(idField, pan.substring(pan.length()-4));
		}
	}
}
