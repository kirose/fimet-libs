package com.fimet.parser;

import java.util.List;

import com.fimet.commons.data.writer.IWriter;
import com.fimet.commons.data.writer.impl.ByteArrayWriter;
import com.fimet.commons.FimetLogger;
import com.fimet.entity.sqlite.EParser;
import com.fimet.iso8583.parser.IFieldParser;
import com.fimet.iso8583.parser.IMessage;
/**
 * 
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 *
 */
public abstract class AbstractMessageLayoutFormater extends AbstractMessageBaseParser {

	public AbstractMessageLayoutFormater(EParser entity) {
		super(entity);
	}

	@Override
	public byte[] formatMessage(IMessage msg) {
		IWriter writer = new ByteArrayWriter();
		formatFields(writer, msg);
		byte[] iso = getConverter().deconvert(getConverter().deconvert(writer.getBytes()));
		return iso;
	}
	protected void formatFields(IWriter writer, IMessage msg) {
		List<IFieldParser> roots = getFieldParserManager().getRootFieldParsers(getIdGroup());
		for (IFieldParser parser : roots) {
			try {
				parser.format(writer, msg);
			} catch (Exception e) {
				FimetLogger.error(this+" error formating field "+parser.getIdField(),e);
				throw e;
			}
		}
	}
}
