package com.fimet.simulator.field;

import java.util.Date;

import com.fimet.commons.utils.DateUtils;
import com.fimet.iso8583.parser.Message;
import com.fimet.simulator.ISimulatorField;

public class SetNewDatehhmmss implements ISimulatorField {
	private static SetNewDatehhmmss instance;
	public static SetNewDatehhmmss getInstance() {
		if (instance == null) {
			instance = new SetNewDatehhmmss();
		}
		return instance;
	}
	@Override
	public void simulate(Message message, String idField) {
		message.setValue(idField, DateUtils.hhmmss_FMT.format(new Date()));
	}
}
