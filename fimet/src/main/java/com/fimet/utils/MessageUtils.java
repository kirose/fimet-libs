package com.fimet.utils;


import static com.fimet.json.JsonAdapterFactory.GSON_PRETTY;

import java.util.Map.Entry;

import com.fimet.IAdapterManager;
import com.fimet.IParserManager;
import com.fimet.Manager;
import com.fimet.json.JsonAdapterFactory;
import com.fimet.json.MessageJson;
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

	public static String toJsonPretty(IMessage message) {
		return JsonAdapterFactory.GSON_PRETTY.toJson(message);
	}
	public static String toJson(IMessage message) {
		return JsonAdapterFactory.GSON.toJson(message);
	}
	static Message fromJson(MessageJson json, IParser parser) {
		Message message = new Message();
		for (Entry<String, String> e : json.getProperties().entrySet()) {
			if (IMessage.PARSER.equals(e.getKey())) {
				message.setProperty(e.getKey(), parserManager.getParser(e.getValue()));	
			} else if (IMessage.ADAPTER.equals(e.getKey())) {
				message.setProperty(e.getKey(), adapterManager.getAdapter(e.getValue()));
			} else {
				message.setProperty(e.getKey(), e.getValue());
			}
		}
		if (parser != null) {
			message.setParser(parser);
		}
		for (Entry<String, String> e : json.getFields().entrySet()) {
			if (e.getValue() != null) {
				message.setValue(e.getKey(), e.getValue());
			}
		}
		return message;
	}
	public static Message fromJson(MessageJson json) {
		return fromJson(json, null);
	}
	public static Message fromJson(String json) {
		MessageJson messageJson = GSON_PRETTY.fromJson(json, MessageJson.class);
		return fromJson(messageJson);
	}
	public static byte[] format(IMessage msg) {
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
	public static IMessage fromString(String message, String parser) {
		if (message.matches(REGEXP_JSON)) {
			return fromJson(message);
		}
		return fromString(message, parserManager.getParser(parser));
	}
	public static IMessage fromBytes(byte[] message, String parserName) {
		return fromBytes(message, parserManager.getParser(parserName));
	}
	public static IMessage fromBytes(byte[] message, IParser parser) {
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
	public static IMessage fromString(String message, IParser parser) {
		IStringAdapter adapter = adapterManager.adapterFor(message);
		IMessage parseMessage = parser.parseMessage(adapter.readString(message));
		parseMessage.setProperty(Message.ADAPTER, adapter);
		return parseMessage;
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
