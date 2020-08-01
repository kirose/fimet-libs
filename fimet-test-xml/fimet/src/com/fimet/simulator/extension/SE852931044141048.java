package com.fimet.simulator.extension;

import com.fimet.simulator.AbstractSimulatorExtension;
import com.fimet.simulator.ISimulator;
import com.fimet.assertions.*;
import static com.fimet.assertions.Assertions.*;
import com.fimet.assertions.IAssertionMaker;
import com.fimet.usecase.IUseCase;
import com.fimet.parser.Message;
import com.fimet.parser.IMessage;
/**
* FIMET
* Code generated automatically
**/
public class SE852931044141048 extends AbstractSimulatorExtension {

	/**
	* @param IMessage msg the icomming message
	**/
	@Override
	public IAssertionResult[] validateIncomingMessage(ISimulator simulator, IMessage msg){
		if ("Oxxo2".equals(simulator.getName())){
			return new IAssertionResult[]{
				exe("DE39",msg,(m)->{return Equals("00",m.getValue(39));})
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
		if ("Oxxo2Iss".equals(simulator.getName())){
			msg.remove("17");
			msg.setValue("54","000000001230");
		}
		return msg;
	}
}
