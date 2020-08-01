package com.fimet.simulator.field;

import java.util.Date;

import com.fimet.parser.IMessage;
import com.fimet.simulator.ISimulatorField;
import com.fimet.utils.DateUtils;

public class IfHasSetNewDatehhmmss implements ISimulatorField {
	public static final IfHasSetNewDatehhmmss INSTANCE = new IfHasSetNewDatehhmmss();
	public static IfHasSetNewDatehhmmss getInstance() {
		return INSTANCE;
	}
	@Override
	public void simulate(IMessage message, String idField) {
		if (message.hasField(idField))
			message.setValue(idField, DateUtils.hhmmss_FMT.format(new Date()));
	}
}
