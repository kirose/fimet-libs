package com.fimet.simulator;

import com.fimet.assertions.IAssertionResult;
import com.fimet.parser.IMessage;

public interface ISimulatorExtension {
	/**
	 * Apply the user validations associated with this socket on write 
	 * @param request message
	 */
	public IAssertionResult[] validateIncomingMessage(ISimulator simulator, IMessage message);
	/**
	 * Apply custom user simulation to responses 
	 * @param request message
	 */
	public IMessage simulateOutgoingMessage(ISimulator simulator, IMessage message);
}
