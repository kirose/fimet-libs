package com.fimet.parser.field.tpv;

import com.fimet.parser.IEFieldFormat;
import com.fimet.parser.IMessage;
import com.fimet.parser.field.VarFieldParser;
import com.fimet.utils.IReader;
import com.fimet.utils.IWriter;

public class TpvTag56VarFieldParser extends VarFieldParser {

	public TpvTag56VarFieldParser(IEFieldFormat fieldFormat) {
		super(fieldFormat);
	}
	@Override
	public byte[] parse(IReader reader, IMessage message) {
		reader.move(2);
		return super.parse(reader,message);
	}
	@Override
	public byte[] format(IWriter writer, IMessage message) {
		writer.append(key.getBytes());
		return super.format(writer, message);
	}
}
