package com.fimet.simulator.field;

import com.fimet.parser.IMessage;
import com.fimet.simulator.ISimulatorField;
import com.fimet.utils.MessageUtils;

public class IfHasSetPanLast4Digits implements ISimulatorField {
	public static final IfHasSetPanLast4Digits INSTANCE = new IfHasSetPanLast4Digits();
	public static IfHasSetPanLast4Digits getInstance() {
		return INSTANCE;
	}
	@Override
	public void simulate(IMessage message, String idField) {
		if (!message.hasField(idField)) {
			return;
		}
		String pan = MessageUtils.getPan(message);
		if (pan != null && pan.length() >= 4) {
			message.setValue(idField, pan.substring(pan.length()-4));
		}
	}
}
