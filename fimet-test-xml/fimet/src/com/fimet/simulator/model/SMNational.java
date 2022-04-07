package com.fimet.simulator.model;

import com.fimet.simulator.ISimulatorModel;
import com.fimet.simulator.AbstractSimulatorModel;
import com.fimet.parser.Message;
import com.fimet.parser.IMessage;
/**
* FIMET
* Code generated automatically
**/
public class SMNational extends AbstractSimulatorModel {

	public SMNational () {
		super("MNational");
	}

	/**
	* @param IMessage msg the outgoing message
	**/
	@Override
	public IMessage simulateRequest(IMessage msg){
		String mti = (String)msg.getProperty("mti");
		if ("0100".equals(mti)){
			com.fimet.simulator.field.SetRandom12AN.getInstance().simulate(msg,"37");
			return msg;
		}
		if ("0200".equals(mti)){
			com.fimet.simulator.field.SetRandom12AN.getInstance().simulate(msg,"37");
			return msg;
		}
		if ("0400".equals(mti)){
			com.fimet.simulator.field.SetRandom12AN.getInstance().simulate(msg,"37");
			return msg;
		}
		if ("0800".equals(mti)){
			msg.setProperty("header","ISO025000077");
			com.fimet.simulator.field.SetNewDateMMddhhmmss.getInstance().simulate(msg,"7");
			com.fimet.simulator.field.SetRandom6N.getInstance().simulate(msg,"11");
			com.fimet.simulator.field.IfHasSetNewDatehhmmss.getInstance().simulate(msg,"12");
			com.fimet.simulator.field.SetRandom12AN.getInstance().simulate(msg,"37");
			return msg;
		}
		return msg;
	}

	/**
	* @param IMessage msg is the incoming message
	**/
	@Override
	public IMessage simulateResponse(IMessage msg){
		String mti = (String)msg.getProperty("mti");
		if ("0100".equals(mti)){
			msg = cloneMessage(msg);
			msg.setProperty("mti",createMtiResponse(mti));
			msg.removeAll("10","13");
			msg.setValue("38","      ");
			msg.setValue("39","00");
			return msg;
		}
		if ("0200".equals(mti)){
			msg = cloneMessage(msg);
			msg.setProperty("mti",createMtiResponse(mti));
			msg.removeAll("10","13");
			msg.setValue("38","      ");
			msg.setValue("39","00");
			return msg;
		}
		if ("0220".equals(mti)){
			msg = cloneMessage(msg);
			msg.setProperty("mti",createMtiResponse(mti));
			msg.removeAll("10","13");
			msg.setValue("38","      ");
			msg.setValue("39","00");
			return msg;
		}
		if ("0420".equals(mti)){
			msg = cloneMessage(msg);
			msg.setProperty("mti",createMtiResponse(mti));
			msg.removeAll("10","13");
			msg.setValue("38","      ");
			msg.setValue("39","00");
			return msg;
		}
		if ("0800".equals(mti)){
			msg = cloneMessage(msg);
			msg.setProperty("mti",createMtiResponse(mti));
			msg.setValue("39","00");
			return msg;
		}
		return null;
	}
}
