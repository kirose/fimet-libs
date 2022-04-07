package com.fimet.simulator;

import com.fimet.assertions.IAssertionResult;
import com.fimet.parser.IMessage;

public class NullSimulatorExtension implements ISimulatorExtension {
	public static final ISimulatorExtension INSTANCE = new NullSimulatorExtension(); 

	@Override
	public IAssertionResult[] validateIncomingMessage(ISimulator socket, IMessage message) {
		return null;
	}

	@Override
	public IMessage simulateOutgoingMessage(ISimulator socket, IMessage message) {
		return message;
	}

}
