package com.fimet.simulator.field;

import java.util.Date;

import com.fimet.commons.utils.DateUtils;
import com.fimet.iso8583.parser.Message;
import com.fimet.simulator.msg.ISimulatorField;

public class SetNewDateyyMMddhhmmss implements ISimulatorField {
	private static SetNewDateyyMMddhhmmss instance;
	public static SetNewDateyyMMddhhmmss getInstance() {
		if (instance == null) {
			instance = new SetNewDateyyMMddhhmmss();
		}
		return instance;
	}
	@Override
	public void simulate(Message message, String idField) {
		message.setValue(idField, DateUtils.yyMMddhhmmss_FMT.format(new Date()));
	}
}
