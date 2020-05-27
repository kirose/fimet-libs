package com.fimet.parser.field.mx;

import com.fimet.entity.EFieldFormat;
import com.fimet.parser.IMessage;
import com.fimet.parser.field.FixedFieldParser;
import com.fimet.parser.numeric.INumericParser;
import com.fimet.parser.numeric.NumericParser;
import com.fimet.utils.converter.Converter;
import com.fimet.utils.converter.IConverter;
import com.fimet.utils.data.IReader;
import com.fimet.utils.data.IWriter;

public class NatTagFixedFieldParser extends FixedFieldParser {

	protected final IConverter  converterLength;
	protected final INumericParser parserLength;
	public NatTagFixedFieldParser(EFieldFormat fieldFormat) {
		super(fieldFormat);
		this.converterLength = Converter.get(fieldFormat.getIdConverterLength());
		this.parserLength = NumericParser.get(fieldFormat.getIdParserLength());
	}
	@Override
	public byte[] parse(IReader reader, IMessage message) {
		reader.move(converterLength.deconvert(key.getBytes()).length+2);
		return super.parse(reader, message);
	}
	@Override
	public byte[] format(IWriter writer, IMessage message) {
		writer.append(converterLength.deconvert(key.getBytes()));
		int index = writer.length();
		writer.move(2);
		byte[] value = super.format(writer, message);
		writer.replace(index, converterLength.deconvert(parserLength.format(value.length, 2)));
		return value;
	}
}
