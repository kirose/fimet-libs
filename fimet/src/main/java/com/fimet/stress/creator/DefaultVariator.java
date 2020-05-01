package com.fimet.stress.creator;

import com.fimet.iso8583.parser.Message;

public class DefaultVariator implements IVariator {
	public static final IVariator INSTANCE = new DefaultVariator();
	@Override
	public Message variate(Message message, FieldVariation[] variation) {
		for (FieldVariation v : variation) {
			if ("mti".equals(v.idField)) {
				message.setMti(v.value);
			} else if ("header".equals(v.idField)) {
				message.setHeader(v.value);
			} else {
				message.setValue(v.idField, v.value);
			}
		}
		return message;
	}
	
} 
