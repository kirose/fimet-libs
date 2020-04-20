package com.fimet.simulator.field;

import com.fimet.commons.utils.PanUtils;
import com.fimet.core.iso8583.parser.Message;
import com.fimet.simulator.msg.ISimulatorField;

public class IfHasSetCorrectPanLastDigit implements ISimulatorField {
	private static IfHasSetCorrectPanLastDigit instance;
	public static IfHasSetCorrectPanLastDigit getInstance() {
		if (instance == null) {
			instance = new IfHasSetCorrectPanLastDigit();
		}
		return instance;
	}
	@Override
	public void simulate(Message message, String idField) {
		if (!message.hasField(idField)) {
			return;
		}
		String pan = message.getPan();
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
