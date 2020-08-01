package com.fimet.simulator.field;

import java.util.Date;

import com.fimet.parser.IMessage;
import com.fimet.simulator.ISimulatorField;
import com.fimet.utils.DateUtils;

public class SetNewDateMMddhhmmss implements ISimulatorField {
	public static final SetNewDateMMddhhmmss INSTANCE = new SetNewDateMMddhhmmss();
	public static SetNewDateMMddhhmmss getInstance() {
		return INSTANCE;
	}
	@Override
	public void simulate(IMessage message, String idField) {
		message.setValue(idField, DateUtils.MMddhhmmss_FMT.format(new Date()));
	}
}
