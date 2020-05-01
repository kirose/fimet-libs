package com.fimet.simulator;

import com.fimet.iso8583.parser.Message;

public interface ISimulatorExtension {
	/**
	 * Apply the user validations associated with this socket on write 
	 * @param request message
	 */
	public ValidationResult[] validateIncomingMessage(ISimulator simulator, Message message);
	/**
	 * Apply custom user simulation to responses 
	 * @param request message
	 */
	public Message simulateOutgoingMessage(ISimulator simulator, Message message);
}
