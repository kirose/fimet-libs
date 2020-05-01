package com.fimet.simulator.msg;

import com.fimet.commons.FimetLogger;
import com.fimet.iso8583.parser.Message;
import com.fimet.simulator.ISimulatorModel;

public class SimulatorMessageResponse extends SimulatorMessage {
	private String mtiResponse;
	public SimulatorMessageResponse(ISimulatorModel simulator, com.fimet.entity.sqlite.ESimulatorMessage sm) {
		super(simulator, sm);
		mtiResponse = String.format("%04d", Integer.parseInt(sm.getMti())+10);
	}
	@Override
	public Message simulate(Message message) {
		if (message != null) {
			if (FimetLogger.isEnabledInfo()) {
				FimetLogger.info(SimulatorMessageResponse.class, "Simulator Response "+simulator +", mti: " + message.getMti());
			}
			Message simulated = message.clone(excludeFields);
			if (simulatedFields != null) {
				for (SimulatorField f : simulatedFields) {
					f.simulate(simulated);
				}
			}
			simulated.setMti(mtiResponse);
			if (header != null) {
				simulated.setHeader(header);
			}
			return simulated;
		}
		return null;
	}
}
