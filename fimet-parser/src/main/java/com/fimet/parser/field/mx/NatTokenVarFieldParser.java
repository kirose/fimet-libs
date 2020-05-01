package com.fimet.parser.field.mx;

import com.fimet.commons.data.reader.IReader;
import com.fimet.commons.data.writer.IWriter;
import com.fimet.commons.exception.FormatException;
import com.fimet.entity.sqlite.EFieldFormat;
import com.fimet.iso8583.parser.IMessage;
import com.fimet.parser.field.VarFieldParser;

public class NatTokenVarFieldParser extends VarFieldParser {

	public NatTokenVarFieldParser(EFieldFormat fieldFormat) {
		super(fieldFormat);
	}
	@Override
	protected byte[] parseValue(IReader reader, IMessage message) {
		reader.assertChar('!');
		reader.assertChar(' ');
		reader.move(2);
		int length = parserLength.parse(converterLength.convert(reader.getBytes(this.length)));
		reader.move(this.length);
		reader.assertChar(' '); 
		byte[] value = reader.read(length);
		message.setField(idField, converterValue.convert(value));
		return value;
	}

	@Override
	protected byte[] formatValue(IWriter writer, IMessage message, byte[] value) {
		writer.append("! ");
		writer.append(key);
		int index = writer.length(); 
		writer.move(length);
		writer.append(" ");

		value = converterValue.deconvert(value);
		writer.append(value);
		
		int length = value.length;
		if (maxLength != null && length > maxLength) {
			throw new FormatException("Field "+this.idField+" ("+length+") Exceed MaxLength: "+ maxLength);
		}
		writer.replace(index, parserLength.format(length, this.length));
		return value;
	}
}
