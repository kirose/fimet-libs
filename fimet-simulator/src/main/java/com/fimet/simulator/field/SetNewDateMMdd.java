package com.fimet.simulator.field;

import java.util.Date;

import com.fimet.parser.IMessage;
import com.fimet.simulator.ISimulatorField;
import com.fimet.utils.DateUtils;

public class SetNewDateMMdd implements ISimulatorField {
	public static final SetNewDateMMdd INSTANCE = new SetNewDateMMdd();
	public static SetNewDateMMdd getInstance() {
		return INSTANCE;
	}
	@Override
	public void simulate(IMessage message, String idField) {
		message.setValue(idField, DateUtils.MMdd_FMT.format(new Date()));
	}
}
