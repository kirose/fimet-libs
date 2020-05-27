package com.fimet.stress.creator;

import com.fimet.parser.IMessage;
import com.fimet.parser.Message;

public class DefaultVariator implements IVariator {
	public static final IVariator INSTANCE = new DefaultVariator();
	@Override
	public IMessage variate(IMessage message, FieldVariation[] variation) {
		for (FieldVariation v : variation) {
			if (Message.MTI.equals(v.idField)) {
				message.setProperty(Message.MTI, v.value);
			} else if (Message.HEADER.equals(v.idField)) {
				message.setProperty(Message.HEADER, v.value);
			} else {
				message.setValue(v.idField, v.value);
			}
		}
		return message;
	}
	
} 
