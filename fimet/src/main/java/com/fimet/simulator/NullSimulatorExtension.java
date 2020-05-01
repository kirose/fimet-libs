package com.fimet.simulator;

import com.fimet.iso8583.parser.Message;

public class NullSimulatorExtension implements ISimulatorExtension {
	public static final ISimulatorExtension INSTANCE = new NullSimulatorExtension(); 

	@Override
	public ValidationResult[] validateIncomingMessage(ISimulator socket, Message message) {
		return null;
	}

	@Override
	public Message simulateOutgoingMessage(ISimulator socket, Message message) {
		return message;
	}

}
