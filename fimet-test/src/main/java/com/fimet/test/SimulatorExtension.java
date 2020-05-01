package com.fimet.test;


import com.fimet.iso8583.parser.Message;
import com.fimet.simulator.AbstractSimulatorExtension;
import com.fimet.simulator.ISimulator;
import com.fimet.simulator.ValidationResult;

/**
 * Custom SimulatorExtension implementation 
 */
public class SimulatorExtension extends AbstractSimulatorExtension {
	public SimulatorExtension() {
	}

	@Override
	public ValidationResult[] validateIncomingMessage(ISimulator simulator, Message message) {
		System.out.println("validations-in-"+simulator+"-"+message.getMti());
		int indexSimulator = indexOf(simulator, message);
		if (indexSimulator == 0){// Acquirer
			return new ValidationResult[]{
				new ValidationResult("Approved", "00".equals(message.getValue(39)))
			};
		}
		return null;
	}

	@Override
	public Message simulateOutgoingMessage(ISimulator simulator, Message message) {
		System.out.println("simulator-ext-request-"+simulator+"-"+message.getMti());
		int index = indexOf(simulator, message);
		if (index == 1) {
			message.setValue("15", "0520");
		}
		return message;
	}

}
