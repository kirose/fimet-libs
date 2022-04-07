package com.fimet.simulator.extension;

import com.fimet.simulator.AbstractSimulatorExtension;
import com.fimet.simulator.ISimulator;
import com.fimet.simulator.ValidationResult;
import com.fimet.simulator.IValidator;
import com.fimet.usecase.UseCase;
import com.fimet.iso8583.parser.Message;
/**
* FIMET
* Code generated automatically
**/
public class SE961 extends AbstractSimulatorExtension {

	/**
	* @param Message msg the icomming message
	**/
	@Override
	public ValidationResult[] validateIncomingMessage(ISimulator simulator, Message msg){
		return null;
	}

	/**
	* This method is executed after Simulator processing
	* @param Message msg is the outgoing message
	**/
	@Override
	public Message simulateOutgoingMessage(ISimulator simulator, Message msg){
		return msg;
	}
}
