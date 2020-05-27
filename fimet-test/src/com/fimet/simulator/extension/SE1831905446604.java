package com.fimet.simulator.extension;

import com.fimet.simulator.AbstractSimulatorExtension;
import com.fimet.simulator.ISimulator;
import com.fimet.simulator.ValidationResult;
import com.fimet.simulator.IValidator;
import com.fimet.usecase.UseCase;
import com.fimet.parser.Message;
import com.fimet.parser.IMessage;
/**
* FIMET
* Code generated automatically
**/
public class SE1831905446604 extends AbstractSimulatorExtension {

	/**
	* @param IMessage msg the icomming message
	**/
	@Override
	public ValidationResult[] validateIncomingMessage(ISimulator simulator, IMessage msg){
		int indexSimulator = indexOf(simulator, msg);
		if (indexSimulator == 0){
			return new ValidationResult[]{
				new ValidationResult("\"00\".equals(msg.getValue(39))", "00".equals(msg.getValue(39)))
			};
		}
		return null;
	}

	/**
	* This method is executed after Simulator processing
	* @param IMessage msg is the outgoing message
	**/
	@Override
	public IMessage simulateOutgoingMessage(ISimulator simulator, IMessage msg){
		return msg;
	}
}
