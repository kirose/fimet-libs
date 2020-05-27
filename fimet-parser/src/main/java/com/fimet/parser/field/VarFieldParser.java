package com.fimet.parser.field;


import com.fimet.FimetLogger;
import com.fimet.entity.EFieldFormat;
import com.fimet.parser.AbstractFieldParser;
import com.fimet.parser.FormatException;
import com.fimet.parser.IMessage;
import com.fimet.parser.numeric.INumericParser;
import com.fimet.parser.numeric.NumericParser;
import com.fimet.utils.converter.Converter;
import com.fimet.utils.converter.IConverter;
import com.fimet.utils.data.IReader;
import com.fimet.utils.data.IWriter;

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
	
	public VarFieldParser(EFieldFormat fieldFormat) {
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
