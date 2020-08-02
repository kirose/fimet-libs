package com.fimet.simulator.model;

import com.fimet.parser.IMessage;
import com.fimet.parser.Message;
import com.fimet.simulator.AbstractSimulatorModel;
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
	public IMessage simulateRequest(IMessage msg){
		String mti = (String)msg.getProperty(Message.MTI);
		if ("0800".equals(mti)){
			msg.setProperty(Message.HEADER, "010200B10000000000000000000000000000000000");
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
		public IMessage simulateResponse(IMessage msg){
		String mti = (String)msg.getProperty(Message.MTI);
		if ("0100".equals(mti)){
			msg = cloneMessage(msg);
			msg.setProperty(Message.MTI, String.format("%04d", Integer.parseInt(mti)+10));
			msg.removeAll("14","18","22","35","38","39","43","44","55","59","104","60","126");
			com.fimet.simulator.field.SetRandom6N.getInstance().simulate(msg,"38");
			msg.setValue("39","00");
			return msg;
		}
		if ("0200".equals(mti)){
			msg = cloneMessage(msg);
			msg.setProperty(Message.MTI, String.format("%04d", Integer.parseInt(mti)+10));
			msg.removeAll("14","18","22","35","38","39","43","44","55","59","104","60","126");
			com.fimet.simulator.field.SetRandom6N.getInstance().simulate(msg,"38");
			msg.setValue("39","00");
			return msg;
		}
		if ("0120".equals(mti)){
			msg = cloneMessage(msg);
			msg.setProperty(Message.MTI, String.format("%04d", Integer.parseInt(mti)+10));
			com.fimet.simulator.field.SetRandom6N.getInstance().simulate(msg,"38");
			msg.setValue("39","00");
			return msg;
		}
		if ("0400".equals(mti)){
			msg = cloneMessage(msg);
			msg.setProperty(Message.MTI, String.format("%04d", Integer.parseInt(mti)+10));
			msg.removeAll("14","18","22","43","60","59","38");
			msg.setValue("39","00");
			return msg;
		}
		if ("0420".equals(mti)){
			msg = cloneMessage(msg);
			msg.setProperty(Message.MTI, String.format("%04d", Integer.parseInt(mti)+10));
			msg.removeAll("14","18","22","43","60","59","38");
			msg.setValue("39","00");
			return msg;
		}
		if ("0800".equals(mti)){
			msg = cloneMessage(msg);
			msg.setProperty(Message.MTI, String.format("%04d", Integer.parseInt(mti)+10));
			msg.setValue("39","00");
			return msg;
		}
		return null;
	}
}