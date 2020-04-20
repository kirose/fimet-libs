package com.fimet.parser.field;


import com.fimet.commons.converter.Converter;
import com.fimet.commons.converter.IConverter;
import com.fimet.commons.data.reader.IReader;
import com.fimet.commons.data.writer.IWriter;
import com.fimet.commons.exception.FormatException;
import com.fimet.commons.numericparser.INumericParser;
import com.fimet.commons.numericparser.NumericParser;
import com.fimet.commons.FimetLogger;
import com.fimet.core.entity.sqlite.FieldFormat;
import com.fimet.core.iso8583.parser.IMessage;
import com.fimet.parser.AbstractFieldParser;

/**
 * Parser for MessageFields from the message 
 * 
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 *
 */
public class VarFieldParser extends AbstractFieldParser {

	protected Integer maxLength;
	protected final IConverter  converterLength;
	protected final INumericParser parserLength;
	
	public VarFieldParser(FieldFormat fieldFormat) {
		super(fieldFormat);
		this.maxLength = fieldFormat.getMaxLength();
		this.converterLength = Converter.get(fieldFormat.getIdConverterLength());
		this.parserLength = NumericParser.get(fieldFormat.getIdParserLength());
	}
	@Override
	protected byte[] parseValue(IReader reader, IMessage message) {
		int length = parserLength.parse(converterLength.convert(reader.read(this.length)));
		byte[] bytes = reader.read(length);
		if (bytes.length != length)
			FimetLogger.warning(this+", expected length "+length+" current length: "+bytes.length);
		return converterValue.convert(bytes);
	}
	@Override
	protected byte[] formatValue(IWriter writer, IMessage message, byte[] value) {
		int index = writer.length(); 
		writer.move(this.length);

		value = converterValue.deconvert(value);
		writer.append(value);
		
		int length = value.length;
		if (maxLength != null && length > maxLength) {
			throw new FormatException("Field "+this.idField+" ("+length+") Exceed MaxLength: "+ maxLength);
		}
		writer.replace(index, converterLength.deconvert(parserLength.format(length, this.length)));
		return value;
	}
	public int getMaxLength() {
		return maxLength;
	}
	public boolean isValidLength(String value) {
		try {
			return converterValue.deconvert(value.getBytes()).length <= maxLength;
		} catch (Exception e) {
			return false;
		}
	}
}
