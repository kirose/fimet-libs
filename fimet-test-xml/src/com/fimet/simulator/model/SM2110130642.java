package com.fimet.simulator.model;

import com.fimet.simulator.ISimulatorModel;
import com.fimet.simulator.AbstractSimulatorModel;
import com.fimet.parser.Message;
import com.fimet.parser.IMessage;
/**
* FIMET
* Code generated automatically
**/
public class SM2110130642 extends AbstractSimulatorModel {

	public SM2110130642 () {
		super("National");
	}

	/**
	* @param IMessage msg the outgoing message
	**/
	@Override
	public IMessage simulateRequest(IMessage msg){
		String mti = (String)msg.getProperty("mti");
		if ("0100".equals(mti)){
			com.fimet.simulator.field.IfHasSetCorrectPanLastDigit.getInstance().simulate(msg,"2");
			com.fimet.simulator.field.IfHasSetNewDateMMddhhmmss.getInstance().simulate(msg,"7");
			com.fimet.simulator.field.IfHasSetNewDatehhmmss.getInstance().simulate(msg,"12");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"13");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"15");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"16");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"17");
			return msg;
		}
		if ("0200".equals(mti)){
			com.fimet.simulator.field.IfHasSetCorrectPanLastDigit.getInstance().simulate(msg,"2");
			com.fimet.simulator.field.IfHasSetNewDateMMddhhmmss.getInstance().simulate(msg,"7");
			com.fimet.simulator.field.IfHasSetNewDatehhmmss.getInstance().simulate(msg,"12");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"13");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"15");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"16");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"17");
			return msg;
		}
		if ("0220".equals(mti)){
			com.fimet.simulator.field.IfHasSetCorrectPanLastDigit.getInstance().simulate(msg,"2");
			com.fimet.simulator.field.IfHasSetNewDateMMddhhmmss.getInstance().simulate(msg,"7");
			com.fimet.simulator.field.IfHasSetNewDatehhmmss.getInstance().simulate(msg,"12");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"13");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"15");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"16");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"17");
			return msg;
		}
		if ("0221".equals(mti)){
			com.fimet.simulator.field.IfHasSetCorrectPanLastDigit.getInstance().simulate(msg,"2");
			com.fimet.simulator.field.IfHasSetNewDateMMddhhmmss.getInstance().simulate(msg,"7");
			com.fimet.simulator.field.IfHasSetNewDatehhmmss.getInstance().simulate(msg,"12");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"13");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"15");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"16");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"17");
			return msg;
		}
		if ("0420".equals(mti)){
			com.fimet.simulator.field.IfHasSetCorrectPanLastDigit.getInstance().simulate(msg,"2");
			com.fimet.simulator.field.IfHasSetNewDateMMddhhmmss.getInstance().simulate(msg,"7");
			com.fimet.simulator.field.IfHasSetNewDatehhmmss.getInstance().simulate(msg,"12");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"13");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"15");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"16");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"17");
			return msg;
		}
		if ("0421".equals(mti)){
			com.fimet.simulator.field.IfHasSetCorrectPanLastDigit.getInstance().simulate(msg,"2");
			com.fimet.simulator.field.IfHasSetNewDateMMddhhmmss.getInstance().simulate(msg,"7");
			com.fimet.simulator.field.IfHasSetNewDatehhmmss.getInstance().simulate(msg,"12");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"13");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"15");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"16");
			com.fimet.simulator.field.IfHasSetNewDateMMdd.getInstance().simulate(msg,"17");
			return msg;
		}
		if ("0800".equals(mti)){
			com.fimet.simulator.field.IfHasSetNewDatehhmmss.getInstance().simulate(msg,"12");
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
			com.fimet.simulator.field.SetNewDateMMdd.getInstance().simulate(msg,"15");
			com.fimet.simulator.field.SetRandom6N.getInstance().simulate(msg,"38");
			msg.setValue("39","00");
			return msg;
		}
		if ("0200".equals(mti)){
			msg = cloneMessage(msg);
			msg.setProperty("mti",String.format("%04d", Integer.parseInt(mti)+10));
			com.fimet.simulator.field.SetNewDateMMdd.getInstance().simulate(msg,"15");
			com.fimet.simulator.field.SetRandom6N.getInstance().simulate(msg,"38");
			msg.setValue("39","00");
			return msg;
		}
		if ("0220".equals(mti)){
			msg = cloneMessage(msg);
			msg.setProperty("mti",String.format("%04d", Integer.parseInt(mti)+10));
			msg.removeAll("12","13","15","17","38","43","48","60","62");
			msg.setValue("39","00");
			return msg;
		}
		if ("0221".equals(mti)){
			msg = cloneMessage(msg);
			msg.setProperty("mti",String.format("%04d", Integer.parseInt(mti)+10));
			msg.removeAll("12","13","15","17","38","43","48","60","62");
			msg.setValue("39","00");
			return msg;
		}
		if ("0420".equals(mti)){
			msg = cloneMessage(msg);
			msg.setProperty("mti",String.format("%04d", Integer.parseInt(mti)+10));
			msg.removeAll("12","13","15","17","38","43","48","60","62");
			msg.setValue("39","00");
			return msg;
		}
		if ("0421".equals(mti)){
			msg = cloneMessage(msg);
			msg.setProperty("mti",String.format("%04d", Integer.parseInt(mti)+10));
			msg.removeAll("12","13","15","17","38","43","48","60","62");
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
