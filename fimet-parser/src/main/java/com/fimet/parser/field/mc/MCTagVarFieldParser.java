package com.fimet.parser.field.mc;

import com.fimet.commons.data.reader.IReader;
import com.fimet.commons.data.writer.IWriter;
import com.fimet.entity.sqlite.EFieldFormat;
import com.fimet.iso8583.parser.IMessage;
import com.fimet.parser.field.VarFieldParser;

public class MCTagVarFieldParser extends VarFieldParser {

	public MCTagVarFieldParser(EFieldFormat fieldFormat) {
		super(fieldFormat);
	}
	@Override
	public byte[] parse(IReader reader, IMessage message) {
		reader.move(key.length());
		return super.parse(reader, message);
	}
	@Override
	public byte[] format(IWriter writer, IMessage message) {
		writer.append(converterValue.deconvert(key.getBytes()));
		return super.format(writer, message);
	}
}
