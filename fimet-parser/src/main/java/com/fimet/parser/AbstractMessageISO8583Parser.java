package com.fimet.parser;

import java.util.ArrayList;
import java.util.List;

import com.fimet.commons.converter.Converter;
import com.fimet.commons.data.reader.IReader;
import com.fimet.commons.data.reader.impl.ByteArrayReader;
import com.fimet.commons.FimetLogger;
import com.fimet.core.IFieldParserManager;
import com.fimet.core.entity.sqlite.Parser;
import com.fimet.core.iso8583.parser.Message;
/**
 * Una clase abstracta que genera un parseo general de un mensaje ISO-8583 a un Message
 * Unicamente se parsean campos presentes en el Bitmap
 *    
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 *
 */
public abstract class AbstractMessageISO8583Parser extends AbstractMessageISO8583Formater {
	public AbstractMessageISO8583Parser(Parser entity) {
		super(entity);
	}
	@Override
	public Message parseMessage(byte[] bytes) {
		Message msg = new Message();
		msg.setParser(this);
		IReader reader = new ByteArrayReader(getConverter().convert(bytes));
		parseHeader(reader, msg);
		int[] bitmap = parseBitmap(reader);
		parseFields(reader, bitmap, msg);
		return msg;
	}
	protected void parseHeader(IReader reader, Message msg) {
		String headerValue = new String(reader.read(16));// ISO0234000700200
		msg.setHeader(headerValue.substring(0, 12));
		msg.setMti(headerValue.substring(12, 16));
	}
	protected int[] parseBitmap(IReader reader) {
		List<Integer> bitmap = new ArrayList<>();
		String bm;
		int noField = 2;
		do {
			bm = new String(Converter.hexToBinary(reader.read(16)));
			for (int i = 1; i < bm.length(); i++) {
				if (bm.charAt(i) == '1') {
					bitmap.add(noField);
				}
				noField++;
			}
			noField++;
		} while (bm.charAt(0) == '1');
		int[] bitmapArray = new int[bitmap.size()];
		int i = 0;
		for (Integer id : bitmap) {
			bitmapArray[i++] = id;
		}
		return bitmapArray;
	}
	protected void parseFields(IReader reader, int[] bitmap, Message message) {
		Integer index = null;
		try {
			IFieldParserManager manager = getFieldParserManager();
			for (int id : bitmap) {
				manager.parse(id, reader, message);
			}
		} catch (Exception e) {
			if (getFailOnError()) {
				throw e;
			} else {
				FimetLogger.warning(this+" error parsing field "+index,e);
			}
		}
	}
	private boolean getFailOnError() {
		return false;//PreferenceDAO.getBoolean(PreferenceDAO.FAIL_ON_PARSE_FIELD_ERROR, Parser.DEFAUT_FAIL_ON_ERROR);
	}
}
