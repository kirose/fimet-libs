package com.fimet.simulator.field;

import java.util.Date;

import com.fimet.commons.utils.DateUtils;
import com.fimet.iso8583.parser.Message;
import com.fimet.simulator.ISimulatorField;

public class IfHasSetNewDateyyMMddhhmmss implements ISimulatorField {
	private static IfHasSetNewDateyyMMddhhmmss instance;
	public static IfHasSetNewDateyyMMddhhmmss getInstance() {
		if (instance == null) {
			instance = new IfHasSetNewDateyyMMddhhmmss();
		}
		return instance;
	}
	@Override
	public void simulate(Message message, String idField) {
		if (message.hasField(idField))
			message.setValue(idField, DateUtils.yyMMddhhmmss_FMT.format(new Date()));
	}
}
