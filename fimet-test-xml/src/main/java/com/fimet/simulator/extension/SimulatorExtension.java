package com.fimet.simulator.extension;


import com.fimet.assertions.Assertions;
import com.fimet.assertions.IAssertionResult;
import com.fimet.parser.IMessage;
import com.fimet.simulator.AbstractSimulatorExtension;
import com.fimet.simulator.ISimulator;

/**
 * Custom SimulatorExtension implementation 
 */
public class SimulatorExtension extends AbstractSimulatorExtension {
	public SimulatorExtension() {
	}

	@Override
	public IAssertionResult[] validateIncomingMessage(ISimulator simulator, IMessage message) {
		//System.out.println("validations-in-"+simulator+"-"+message.getMti());
		if ("Aquirer".equals(simulator.getName())){// Acquirer
			return new IAssertionResult[]{
				Assertions.Equals("00", message.getValue(39)).execute("DE39")
			};
		}
		return null;
	}

	@Override
	public IMessage simulateOutgoingMessage(ISimulator simulator, IMessage message) {
		//System.out.println("simulator-ext-request-"+simulator+"-"+message.getMti());
		if ("Issuer".equals(simulator.getName())) {
			message.setValue("15", "0520");
		}
		return message;
	}

}
