package com.fimet.parser.field.tpv;


import com.fimet.commons.data.reader.IReader;
import com.fimet.commons.data.reader.impl.ByteArrayReader;
import com.fimet.commons.data.writer.IWriter;
import com.fimet.commons.FimetLogger;
import com.fimet.entity.sqlite.EFieldFormat;
import com.fimet.iso8583.parser.IMessage;
import com.fimet.parser.field.VarFieldParser;

public class TpvTags56VarFieldParser extends VarFieldParser {

	public TpvTags56VarFieldParser(EFieldFormat fieldFormat) {
		super(fieldFormat);
	}
	protected void parseChilds(byte[] value, IMessage message) {
		if (childs != null && message.getField(idField) != null) {
			try {
				IReader reader = new ByteArrayReader(message.getField(idField));
				if (!reader.hasNext()) {
					return;
				}
				parseTags(message, reader);
			} catch (Exception e) {
				if (getFailOnError()) {
					throw e;
				} else {
					FimetLogger.warning("Parsing tokens "+idField,e);
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
		String nextTag = new String(reader.getBytes(2));
		return getFieldParserManager().getFieldParser(getGroup(),idField+"."+nextTag).parse(reader, message);
	}
	@Override
	protected void formatChilds(IWriter writer, IMessage message) {
		for (String idChild : message.getIdChildren(idField)) {
			getFieldParserManager().format(message, idChild, writer);
		}
	}
}
