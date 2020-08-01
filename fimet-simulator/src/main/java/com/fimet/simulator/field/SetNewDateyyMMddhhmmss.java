package com.fimet.simulator.field;

import java.util.Date;

import com.fimet.parser.IMessage;
import com.fimet.simulator.ISimulatorField;
import com.fimet.utils.DateUtils;

public class SetNewDateyyMMddhhmmss implements ISimulatorField {
	public static final SetNewDateyyMMddhhmmss INSTANCE = new SetNewDateyyMMddhhmmss();
	public static SetNewDateyyMMddhhmmss getInstance() {
		return INSTANCE;
	}
	@Override
	public void simulate(IMessage message, String idField) {
		message.setValue(idField, DateUtils.yyMMddhhmmss_FMT.format(new Date()));
	}
}
