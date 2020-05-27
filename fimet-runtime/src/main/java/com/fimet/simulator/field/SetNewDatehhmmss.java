package com.fimet.simulator.field;

import java.util.Date;

import com.fimet.parser.IMessage;
import com.fimet.simulator.ISimulatorField;
import com.fimet.utils.DateUtils;

public class SetNewDatehhmmss implements ISimulatorField {
	public static final SetNewDatehhmmss INSTANCE = new SetNewDatehhmmss();
	public static SetNewDatehhmmss getInstance() {
		return INSTANCE;
	}
	@Override
	public void simulate(IMessage message, String idField) {
		message.setValue(idField, DateUtils.hhmmss_FMT.format(new Date()));
	}
}
