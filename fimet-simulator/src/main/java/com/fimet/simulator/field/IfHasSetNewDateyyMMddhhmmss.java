package com.fimet.simulator.field;

import java.util.Date;

import com.fimet.parser.IMessage;
import com.fimet.simulator.ISimulatorField;
import com.fimet.utils.DateUtils;

public class IfHasSetNewDateyyMMddhhmmss implements ISimulatorField {
	public static final IfHasSetNewDateyyMMddhhmmss INSTANCE = new IfHasSetNewDateyyMMddhhmmss();
	public static IfHasSetNewDateyyMMddhhmmss getInstance() {
		return INSTANCE;
	}
	@Override
	public void simulate(IMessage message, String idField) {
		if (message.hasField(idField))
			message.setValue(idField, DateUtils.yyMMddhhmmss_FMT.format(new Date()));
	}
}
