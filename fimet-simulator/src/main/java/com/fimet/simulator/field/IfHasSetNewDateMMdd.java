package com.fimet.simulator.field;

import java.util.Date;

import com.fimet.parser.IMessage;
import com.fimet.simulator.ISimulatorField;
import com.fimet.utils.DateUtils;

public class IfHasSetNewDateMMdd implements ISimulatorField {
	public static final IfHasSetNewDateMMdd INSTANCE = new IfHasSetNewDateMMdd();
	public static IfHasSetNewDateMMdd getInstance() {
		return INSTANCE;
	}
	@Override
	public void simulate(IMessage message, String idField) {
		if (message.hasField(idField))
			message.setValue(idField, DateUtils.MMdd_FMT.format(new Date()));
	}
}
