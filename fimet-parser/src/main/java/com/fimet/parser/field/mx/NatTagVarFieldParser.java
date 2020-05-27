package com.fimet.parser.field.mx;

import com.fimet.entity.EFieldFormat;
import com.fimet.parser.IMessage;
import com.fimet.parser.field.VarFieldParser;
import com.fimet.utils.data.IReader;
import com.fimet.utils.data.IWriter;

public class NatTagVarFieldParser extends VarFieldParser {

	public NatTagVarFieldParser(EFieldFormat fieldFormat) {
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
