package com.fimet.test;

import com.fimet.simulator.AbstractSimulatorModel;
import com.fimet.iso8583.parser.Message;
/**
* FIMET
* Code generated automatically
**/
public class SMVisa extends AbstractSimulatorModel {

	public SMVisa () {
		super("Visa");
	}

	/**
	* @param Message msg the outgoing message
	**/
	@Override
	public Message simulateRequest(Message msg){
		if ("0800".equals(msg.getMti())){
			msg.setHeader("010200B10000000000000000000000000000000000");
			com.fimet.simulator.field.SetNewDateMMddhhmmss.getInstance().simulate(msg,"7");
			com.fimet.simulator.field.SetRandom6N.getInstance().simulate(msg,"11");
			msg.setValue("15","0903");
			msg.setValue("48","0111000122S00484");
			msg.setValue("70","301");
			return msg;
		}
		return msg;
	}

	/**
	* @param Message msg is the incoming message
	**/
	@Override
		public Message simulateResponse(Message msg){
		if ("0100".equals(msg.getMti())){
			msg = cloneMessage(msg);
			msg.setMti(String.format("%04d", Integer.parseInt(msg.getMti())+10));
			msg.remove("14","18","22","35","38","39","43","44","55","59","104","60","126");
			com.fimet.simulator.field.SetRandom6N.getInstance().simulate(msg,"38");
			msg.setValue("39","00");
			return msg;
		}
		if ("0120".equals(msg.getMti())){
			msg = cloneMessage(msg);
			msg.setMti(String.format("%04d", Integer.parseInt(msg.getMti())+10));
			com.fimet.simulator.field.SetRandom6N.getInstance().simulate(msg,"38");
			msg.setValue("39","00");
			return msg;
		}
		if ("0400".equals(msg.getMti())){
			msg = cloneMessage(msg);
			msg.setMti(String.format("%04d", Integer.parseInt(msg.getMti())+10));
			msg.remove("14","18","22","43","60","59","38");
			msg.setValue("39","00");
			return msg;
		}
		if ("0800".equals(msg.getMti())){
			msg = cloneMessage(msg);
			msg.setMti(String.format("%04d", Integer.parseInt(msg.getMti())+10));
			msg.setValue("39","00");
			return msg;
		}
		return null;
	}
}