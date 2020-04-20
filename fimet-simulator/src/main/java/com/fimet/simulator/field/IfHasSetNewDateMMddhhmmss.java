package com.fimet.simulator.field;

import java.util.Date;

import com.fimet.commons.utils.DateUtils;
import com.fimet.core.iso8583.parser.Message;
import com.fimet.simulator.msg.ISimulatorField;

public class IfHasSetNewDateMMddhhmmss implements ISimulatorField {
	private static IfHasSetNewDateMMddhhmmss instance;
	public static IfHasSetNewDateMMddhhmmss getInstance() {
		if (instance == null) {
			instance = new IfHasSetNewDateMMddhhmmss();
		}
		return instance;
	}
	@Override
	public void simulate(Message message, String idField) {
		if (message.hasField(idField))
			message.setValue(idField, DateUtils.MMddhhmmss_FMT.format(new Date()));
	}
}
