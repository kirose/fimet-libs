package com.fimet.parser.field.visa;

import com.fimet.commons.data.reader.IReader;
import com.fimet.commons.data.writer.IWriter;
import com.fimet.entity.sqlite.EFieldFormat;
import com.fimet.iso8583.parser.IMessage;
import com.fimet.parser.field.VarFieldParser;

public class VisaTagVarFieldParser extends VarFieldParser {

	public VisaTagVarFieldParser(EFieldFormat fieldFormat) {
		super(fieldFormat);
	}
	@Override
	public byte[] parse(IReader reader, IMessage message) {
		reader.move(key.length());
		return super.parse(reader, message);
	}
	@Override
	public byte[] format(IWriter writer, IMessage message) {
		writer.append(key.getBytes());
		return super.format(writer, message);
	}
}
