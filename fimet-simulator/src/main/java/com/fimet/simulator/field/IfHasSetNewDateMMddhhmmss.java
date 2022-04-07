package com.fimet.simulator.field;

import java.util.Date;

import com.fimet.parser.IMessage;
import com.fimet.simulator.ISimulatorField;
import com.fimet.utils.DateUtils;

public class IfHasSetNewDateMMddhhmmss implements ISimulatorField {
	public static final IfHasSetNewDateMMddhhmmss INSTANCE = new IfHasSetNewDateMMddhhmmss();
	public static IfHasSetNewDateMMddhhmmss getInstance() {
		return INSTANCE;
	}
	@Override
	public void simulate(IMessage message, String idField) {
		if (message.hasField(idField))
			message.setValue(idField, DateUtils.MMddhhmmss_FMT.format(new Date()));
	}
}
