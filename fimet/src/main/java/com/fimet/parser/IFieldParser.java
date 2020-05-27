package com.fimet.parser;

import com.fimet.utils.data.IReader;
import com.fimet.utils.data.IWriter;
/**
 * 
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 *
 */
public interface IFieldParser {
	
	String getName();
	String getIdField();
	String getIdOrder();
	
	String getType();
	int getLength();
	boolean isValidValue(String value);
	boolean isValidLength(String value);
	short[] getAddress();
	
	byte[] parse(IReader reader, IMessage message) throws ParserException;
	byte[] format(IWriter writer, IMessage message) throws FormatException;
	
	boolean hasChild(String idChild);
	int indexOfChild(String idChild);
	String getIdChild(int index);
}
