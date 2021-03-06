package com.fimet.parser.field.mc;


import com.fimet.FimetLogger;
import com.fimet.parser.IEFieldFormat;
import com.fimet.parser.FormatException;
import com.fimet.parser.IMessage;
import com.fimet.parser.ParserException;
import com.fimet.parser.field.VarFieldParser;
import com.fimet.utils.ByteBuffer;
import com.fimet.utils.IReader;
import com.fimet.utils.IWriter;

public class MC48VarFieldParser extends VarFieldParser {

	public MC48VarFieldParser(IEFieldFormat fieldFormat) {
		super(fieldFormat);
	}
	@Override
	protected void parseChilds(byte[] value, IMessage message) {
		if (childs != null) {
			try {
				IReader reader = new ByteBuffer(value);// Tags
				group.parse(idField+"."+"tcc", message, reader);
				parseTags(message, reader);
			} catch (Exception e) {
				if (failOnErrorParseField) {
					throw e;
				} else {
					FimetLogger.warning(this+" error parsing tags "+idField,e);
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
			throw new ParserException(this+" unknow Tag starts with: "+reader.toString().substring(0,5)+".\nTags declared: "+childs);	
		}
		return group.parse(idField+"."+nextTag, message, reader);
	}
	private String getNextTag(IReader reader) {
		for (String tag : childs) {
			if (reader.startsWith(tag)) {
				return tag;
			}
		}
		return null;
	}
	@Override
	protected void formatChilds(IWriter writer, IMessage message) {
		for (String idChild : message.getIdChildren(idField)) {
			validateTag(idChild);
			group.format(idChild, message, writer);
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
