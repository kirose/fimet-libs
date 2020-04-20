package com.fimet.parser.mx;

import com.fimet.commons.data.reader.IReader;
import com.fimet.commons.data.writer.IWriter;
import com.fimet.commons.exception.FormatException;
import com.fimet.core.iso8583.parser.Message;
import com.fimet.parser.AbstractMessageISO8583Parser;

public class TpvParser extends AbstractMessageISO8583Parser{
	public TpvParser(com.fimet.core.entity.sqlite.Parser entity) {
		super(entity);
	}
	@Override
	protected void formatHeader(IWriter writer, Message msg) {
		if (msg.getHeader() == null || msg.getHeader().length() <= 0) {
			throw new FormatException("ISO header section invalid (length = 0)");
		}
		if (msg.getHeader().length() != 10) {
			throw new FormatException("ISO header section invalid (length = "+msg.getHeader().length()+") expected length = 10");
		}
		if (msg.getMti().length() != 4) {
			throw new FormatException("MTI invalid expected length 4 found('"+msg.getMti().length()+"'): '"+msg.getMti()+"'");
		}
		String value = msg.getHeader()+msg.getMti();
		writer.append(value.getBytes());
	}
	@Override
	protected void parseHeader(IReader reader, Message msg) {
		String headerValue = new String(reader.read(14));
		msg.setHeader(headerValue.substring(0, 10));
		msg.setMti(headerValue.substring(10, 14));
	}
}
