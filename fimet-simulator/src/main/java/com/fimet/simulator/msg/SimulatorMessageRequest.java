package com.fimet.simulator.msg;


import com.fimet.commons.FimetLogger;
import com.fimet.core.iso8583.parser.Message;
import com.fimet.core.simulator.ISimulator;

public class SimulatorMessageRequest extends SimulatorMessage {
	public SimulatorMessageRequest(ISimulator simulator, com.fimet.core.entity.sqlite.SimulatorMessage sm) {
		super(simulator, sm);
	}
	@Override
	public Message simulate(Message message) {
		if (message != null) {
			if (FimetLogger.isEnabledInfo()) {
				FimetLogger.info(SimulatorMessageRequest.class, "Simulator Request "+simulator.toString() +", mti: " + message.getMti());
			}
			if (simulatedFields != null) {
				for (SimulatorField f : simulatedFields) {
					f.simulate(message);
				}
			}
			if (header != null) {
				message.setHeader(header);
			}
			return message;
		}
		return null;
	}
}
