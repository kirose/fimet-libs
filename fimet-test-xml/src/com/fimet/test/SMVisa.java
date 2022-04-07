package com.fimet.test;

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
		super("MNational");
	}

	/**
	* @param IMessage msg the outgoing message
	**/
	@Override
	public IMessage simulateRequest(IMessage msg){
		String mti = (String)msg.getProperty("mti");
		if ("0100".equals(mti)){
			com.fimet.simulator.field.SetRandom6AN.getInstance().simulate(msg,"37");
			return msg;
		}
		if ("0200".equals(mti)){
			com.fimet.simulator.field.SetRandom6AN.getInstance().simulate(msg,"37");
			return msg;
		}
		if ("0400".equals(mti)){
			com.fimet.simulator.field.SetRandom6AN.getInstance().simulate(msg,"37");
			return msg;
		}
		if ("0800".equals(mti)){
			msg.setProperty("header","ISO025000077");
			com.fimet.simulator.field.SetNewDateMMddhhmmss.getInstance().simulate(msg,"7");
			com.fimet.simulator.field.SetRandom6N.getInstance().simulate(msg,"11");
			com.fimet.simulator.field.IfHasSetNewDatehhmmss.getInstance().simulate(msg,"12");
			com.fimet.simulator.field.SetRandom6AN.getInstance().simulate(msg,"37");
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
			msg.setProperty("mti",String.format("%04d", Integer.parseInt(mti)+10));
			msg.removeAll("10","13");
			msg.setValue("37","            ");
			msg.setValue("39","00");
			return msg;
		}
		if ("0200".equals(mti)){
			msg = cloneMessage(msg);
			msg.setProperty("mti",String.format("%04d", Integer.parseInt(mti)+10));
			msg.removeAll("10","13");
			msg.setValue("37","            ");
			msg.setValue("39","00");
			return msg;
		}
		if ("0420".equals(mti)){
			msg = cloneMessage(msg);
			msg.setProperty("mti",String.format("%04d", Integer.parseInt(mti)+10));
			msg.removeAll("10","13");
			msg.setValue("37","            ");
			msg.setValue("39","00");
			return msg;
		}
		if ("0800".equals(mti)){
			msg = cloneMessage(msg);
			msg.setProperty("mti",String.format("%04d", Integer.parseInt(mti)+10));
			msg.setValue("39","00");
			return msg;
		}
		return null;
	}
}
