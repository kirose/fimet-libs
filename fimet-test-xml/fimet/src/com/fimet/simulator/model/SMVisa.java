package com.fimet.simulator.model;

import com.fimet.simulator.ISimulatorModel;
import com.fimet.simulator.AbstractSimulatorModel;
import com.fimet.parser.Message;
import com.fimet.parser.IMessage;
/**
* FIMET
* Code generated automatically
**/
public class SMVisa extends AbstractSimulatorModel {

	public SMVisa () {
		super("MVisa");
	}

	/**
	* @param IMessage msg the outgoing message
	**/
	@Override
	public IMessage simulateRequest(IMessage msg){
		return msg;
	}

	/**
	* @param IMessage msg is the incoming message
	**/
	@Override
	public IMessage simulateResponse(IMessage msg){
		return null;
	}
}
