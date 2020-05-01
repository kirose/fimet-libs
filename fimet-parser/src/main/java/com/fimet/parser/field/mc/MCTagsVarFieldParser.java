package com.fimet.parser.field.mc;

import com.fimet.commons.data.reader.IReader;
import com.fimet.commons.data.reader.impl.ByteArrayReader;
import com.fimet.commons.data.writer.IWriter;
import com.fimet.commons.exception.FormatException;
import com.fimet.commons.exception.ParserException;
import com.fimet.commons.FimetLogger;
import com.fimet.entity.sqlite.EFieldFormat;
import com.fimet.iso8583.parser.IMessage;
import com.fimet.parser.field.VarFieldParser;

public class MCTagsVarFieldParser extends VarFieldParser {

	public MCTagsVarFieldParser(EFieldFormat fieldFormat) {
		super(fieldFormat);
	}
	@Override
	protected void parseChilds(byte[] value, IMessage message) {
		if (childs != null) {
			try {
				IReader reader = new ByteArrayReader(value);
				parseTags(message, reader);
			} catch (Exception e) {
				if (getFailOnError()) {
					throw e;
				} else {
					FimetLogger.warning("Parsing tags "+idField,e);
				}
			}
		}
	}
	private void parseTags(IMessage message, IReader reader) {
		while (reader.hasNext()) {
			parseTag(reader, message);
		}
	}
	private byte[] parseTag(IReader reader, IMessage message) {
		String nextTag = getNextTag(reader);
		if (nextTag == null) {
			throw new ParserException("Unknow Tag starts with: "+reader.toString().substring(0,5)+".Tags declared: "+childs);	
		}
		return getFieldParserManager().getFieldParser(getGroup(),idField+"."+nextTag).parse(reader, message);
	}
	private String getNextTag(IReader reader) {
		for (String tag : childs) {
			if (reader.matcher(tag).asByte()) {
				return tag;
			}
		}
		return null;
	}
	@Override
	protected void formatChilds(IWriter writer, IMessage message) {
		for (String idChild : message.getIdChildren(idField)) {
			validateTag(idChild);
			getFieldParserManager().format(message, idChild, writer);
		}
	}
	private void validateTag(String idField) {
		String tag = idField.substring(idField.lastIndexOf('.')+1);
		for (String child : childs) {
			if (child.equals(tag)) {
				return;
			}
		}
		throw new FormatException("Unexpected Tag '"+tag+"', Tags declared: "+childs);
	}
}
