package com.fimet.parser;

import com.fimet.utils.data.IReader;
import com.fimet.utils.data.IWriter;

public interface IParser {

	public static final int ISO8583 = 0;
	public static final int LAYOUT = 1;

	Integer getId();
	IFieldGroup getFieldGroup();
	String getName();
	byte[] formatMessage(IMessage msg) throws FormatException;
	IMessage parseMessage(byte[] bytes) throws ParserException;
	byte[] parseField(String idField, IMessage message, IReader reader) throws ParserException;
	byte[] parseField(int idField, IMessage message, IReader reader) throws ParserException;
	void formatField(String idField, IMessage message, IWriter writer) throws FormatException;
	void formatField(int idField, IMessage message, IWriter writer) throws FormatException;
}
