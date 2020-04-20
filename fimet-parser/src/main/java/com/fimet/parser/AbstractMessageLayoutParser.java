package com.fimet.parser;

import java.util.List;

import com.fimet.commons.data.reader.IReader;
import com.fimet.commons.data.reader.impl.ByteArrayReader;
import com.fimet.commons.FimetLogger;
import com.fimet.core.entity.sqlite.Parser;
import com.fimet.core.iso8583.parser.IFieldParser;
import com.fimet.core.iso8583.parser.MessageLayout;
/**
 * Una clase abstracta que genera un parseo general de un mensaje ISO-8583 a un Message
 * Unicamente se parsean campos presentes en el Bitmap
 *    
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 *
 */
public abstract class AbstractMessageLayoutParser extends AbstractMessageLayoutFormater {
	public AbstractMessageLayoutParser(Parser entity) {
		super(entity);
	}
	@Override
	public MessageLayout parseMessage(byte[] bytes) {
		MessageLayout msg = new MessageLayout();
		msg.setParser(this);
		IReader reader = new ByteArrayReader(getConverter().convert(bytes));
		parseFields(reader, msg);
		return msg;
	}
	protected void parseFields(IReader reader, MessageLayout message) {
		List<IFieldParser> roots = getFieldParserManager().getRootFieldParsers(getIdGroup());
		IFieldParser last = null;
		try {
			for (IFieldParser parser : roots) {
				last = parser;
				parser.parse(reader, message);
			}
		} catch (Exception e) {
			if (getFailOnError()) {
				throw e;
			} else {
				FimetLogger.warning("Error parsing "+last,e);
			}
		}
	}
	private boolean getFailOnError() {
		return false;//PreferenceDAO.getBoolean(PreferenceDAO.FAIL_ON_PARSE_FIELD_ERROR, Parser.DEFAUT_FAIL_ON_ERROR);
	}
}
