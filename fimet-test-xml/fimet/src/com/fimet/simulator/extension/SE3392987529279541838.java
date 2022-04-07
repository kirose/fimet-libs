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
public class SE3392987529279541838 extends AbstractSimulatorExtension {

	/**
	* @param IMessage msg the icomming message
	**/
	@Override
	public IAssertionResult[] validateIncomingMessage(ISimulator simulator, IMessage msg){
		if ("Oxxo3".equals(simulator.getName())){
			return new IAssertionResult[]{
				exe("DE39",msg,(m)->{return Equals(m.getValue(39),"00");}),
				exe("NotDE39",msg,(m)->{return IsTrue(m.hasField(39));}),
				exe("NotNullDE39",msg,(m)->{return IsNotNull(m.getValue(39));}),
				exe("DE39In00,01",msg,(m)->{return In(m.getValue(39),"00","01");})
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
		if ("Oxxo3Iss".equals(simulator.getName())){
			return null;
		}
		return msg;
	}
}
