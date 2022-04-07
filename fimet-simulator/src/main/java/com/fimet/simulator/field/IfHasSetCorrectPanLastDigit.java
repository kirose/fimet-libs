package com.fimet.simulator.field;

import com.fimet.parser.IMessage;
import com.fimet.simulator.ISimulatorField;
import com.fimet.utils.MessageUtils;
import com.fimet.utils.PanUtils;

public class IfHasSetCorrectPanLastDigit implements ISimulatorField {
	public static final IfHasSetCorrectPanLastDigit INSTANCE = new IfHasSetCorrectPanLastDigit();
	public static IfHasSetCorrectPanLastDigit getInstance() {
		return INSTANCE;
	}
	@Override
	public void simulate(IMessage message, String idField) {
		if (!message.hasField(idField)) {
			return;
		}
		String pan = MessageUtils.getPan(message);
		if (pan != null && pan.length() >= 15) {
			String value = message.getValue(idField);
			if (value != null && value.length() >= pan.length()) {
				char lastDigit = PanUtils.calculateLastDigit(pan);
				value = value.substring(0,pan.length()-1)+lastDigit+value.substring(pan.length());
				message.setValue(idField, value);
			}
		}
	}
}
