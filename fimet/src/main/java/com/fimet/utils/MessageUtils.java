package com.fimet.utils;


import static com.fimet.json.JsonAdapterFactory.GSON_PRETTY;

import java.lang.reflect.Type;
import java.util.Map;


import com.google.gson.reflect.TypeToken;
import com.fimet.IAdapterManager;
import com.fimet.IParserManager;
import com.fimet.Manager;
import com.fimet.parser.IMessage;
import com.fimet.parser.IParser;
import com.fimet.parser.Message;
import com.fimet.parser.IAdapter;
import com.fimet.parser.IByteArrayAdapter;
import com.fimet.parser.IStringAdapter;

/**
 * 
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 *
 */
public class MessageUtils {

	public static final String REGEXP_JSON = "(?s)^\\s*\\{\\s*\\\"[a-zA-Z]+.*";
	private static IAdapterManager adapterManager = Manager.get(IAdapterManager.class);
	private static IParserManager parserManager = Manager.get(IParserManager.class);
	public static Message parseJsonMessage(String json) {
		//gson.toJson(gson.fromJson(json, Message.class))
		return GSON_PRETTY.fromJson(json, Message.class);
	}
	public static byte[] formatMessage(IMessage msg) {
		byte[] bytes = msg.getParser().formatMessage(msg);
		if (msg.hasProperty(Message.ADAPTER)) {
			IAdapter adapter = (IAdapter)msg.getProperty(Message.ADAPTER);
			if (msg.getProperty(Message.ADAPTER) instanceof IByteArrayAdapter) {
				return ((IByteArrayAdapter)adapter).writeByteArray(bytes);
			} else {
				return bytes;
			}
		} else {
			return msg.getParser().formatMessage(msg);
		}
	}
	public static IMessage parseMessage(byte[] bytes, int idParser) {
		return parseMessage(bytes, parserManager.getParser(idParser));
	}
	public static IMessage parseMessage(String message, int idParser) {
		if (message.matches(REGEXP_JSON)) {
			return parseJsonMessage(message);
		}
		return parseMessage(message, parserManager.getParser(idParser));
	}
	public static IMessage parseMessage(String message, String parserName) {
		return parseMessage(message, parserManager.getParser(parserName));
	}
	public static IMessage parseMessage(byte[] message, String parserName) {
		return parseMessage(message, parserManager.getParser(parserName));
	}
	public static IMessage parseMessage(byte[] message, IParser parser) {
		IByteArrayAdapter adapter = adapterManager.adapterFor(message);
		IMessage parseMessage = parser.parseMessage(adapter.readByteArray(message));
		parseMessage.setProperty(Message.ADAPTER, adapter);
		return parseMessage;
	}
	/**
	 * Parse a sim_queue (Hexadecimal) to a Msg
	 * @param msgHex
	 * @param parserName
	 * @return
	 */
	public static IMessage parseMessage(String message, IParser parser) {
		IStringAdapter adapter = adapterManager.adapterFor(message);
		IMessage parseMessage = parser.parseMessage(adapter.readString(message));
		parseMessage.setProperty(Message.ADAPTER, adapter);
		return parseMessage;
	}
	public static Map<String,Object> parseJsonAsMap(String json){
		Type type = new TypeToken<Map<String, Object>>() {}.getType();
		return GSON_PRETTY.fromJson(json, type);
	}
	public static Object parseJson(String json, Type type){
		return GSON_PRETTY.fromJson(json, type);
	}
	public static String getPan(IMessage m) {
		String pan = null;
		if (m.hasField(35)) {// Nacional
			pan = m.getValue(35);
			if (pan.indexOf('=') > 0) {
				pan = pan.substring(0,pan.indexOf('='));
			} else if (pan.indexOf('D') > 0) {
				pan = pan.substring(0,pan.indexOf('D'));
			} else {
				pan = null;
			}
		} else if (m.hasField(2)) {// Visa MasterCard
			pan = m.getValue(2).substring(0,16);
		} else if (m.hasField("63.EZ.10.track2")) {// Int
			pan = m.getValue("63.EZ.10.track2");
			pan = pan.substring(0,pan.indexOf('D'));
		} else if (m.hasField("63.EZ.9.track2")) {// TPV
			pan = m.getValue("63.EZ.9.track2");
			pan = pan.substring(0,pan.indexOf('D'));
		}
		return pan;
	}
	public static void setPan(IMessage m, String pan) {
		if (pan != null && pan.length() > 0 && pan.length() >= 15) {
			if (m.hasField(35)) {// Nacional
				String track2 = m.getValue(35);
				if (track2.indexOf('=') > 0) {
					m.setValue(35,pan+track2.substring(track2.indexOf('=')));
				} else if (track2.indexOf('D') > 0) {
					m.setValue(35,pan+track2.substring(track2.indexOf('D')));
				}
			} else 
			if (m.hasField(2)) {// Visa MasterCard
				m.setValue(2,pan);
			} else
			if (m.hasField("63.EZ.10.track2")) {// Int
				String track2 = m.getValue("63.EZ.10.track2");
				String track2New = pan+track2.substring(track2.indexOf('D'));
				if (track2New.length() > track2.length()) {
					track2New = track2New.substring(0,track2.length());
				} else if (track2New.length() < track2.length()) { 
					track2New = track2New+StringUtils.leftPad("", track2New.length() - track2.length(),'F');
				}
				m.setValue("63.EZ.10.track2", track2);
			} else 
			if (m.hasField("63.EZ.9.track2")) {// TPV
				String track2 = m.getValue("63.EZ.9.track2");
				String track2New = pan+track2.substring(track2.indexOf('D'));
				if (track2New.length() > track2.length()) {
					track2New = track2New.substring(0,track2.length());
				} else if (track2New.length() < track2.length()) { 
					track2New = track2New+StringUtils.leftPad("", track2New.length() - track2.length(),'F');
				}
				m.setValue("63.EZ.9.track2", track2);
			}
		}
	}
}
