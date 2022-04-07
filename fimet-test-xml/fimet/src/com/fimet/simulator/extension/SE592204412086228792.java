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
public class SE592204412086228792 extends AbstractSimulatorExtension {

	/**
	* @param IMessage msg the icomming message
	**/
	@Override
	public IAssertionResult[] validateIncomingMessage(ISimulator simulator, IMessage msg){
		if ("Oxxo3".equals(simulator.getName())){
			return new IAssertionResult[]{
				exe("DE39",msg,(m)->{return Equals(m.getValue(39),"00");}),
				exe("DE38",msg,(m)->{return Equals(m.getValue(38),"000000");})
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
