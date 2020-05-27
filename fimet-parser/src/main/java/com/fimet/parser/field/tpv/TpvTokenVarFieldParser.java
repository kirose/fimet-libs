package com.fimet.parser.field.tpv;

import com.fimet.entity.EFieldFormat;
import com.fimet.parser.FormatException;
import com.fimet.parser.IMessage;
import com.fimet.parser.field.VarFieldParser;
import com.fimet.utils.data.IReader;
import com.fimet.utils.data.IWriter;

public class TpvTokenVarFieldParser extends VarFieldParser {

	public TpvTokenVarFieldParser(EFieldFormat fieldFormat) {
		super(fieldFormat);
	}
	@Override
	public byte[] parse(IReader reader, IMessage message) {
		reader.move(2);
		return super.parse(reader, message);
	}
	@Override
	protected byte[] formatValue(IWriter writer, IMessage message, byte[] value) {
		writer.append(key);
		int index = writer.length(); 
		writer.move(length);

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
