package com.fimet.utils;


import static com.fimet.json.adapter.JsonAdapterFactory.GSON;

import java.lang.reflect.Type;
import java.util.Map;


import com.google.gson.reflect.TypeToken;
import com.fimet.IAdapterManager;
import com.fimet.IParserManager;
import com.fimet.Manager;
import com.fimet.adapter.IByteArrayAdapter;
import com.fimet.adapter.IStringAdapter;
import com.fimet.commons.exception.AdapterException;
import com.fimet.iso8583.parser.IMessage;
import com.fimet.iso8583.parser.IParser;
import com.fimet.iso8583.parser.Message;

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
		return GSON.fromJson(json, Message.class);
	}
	public static byte[] formatMessage(Message msg) {
		if (msg.getAdapter() instanceof IByteArrayAdapter) {
			return ((IByteArrayAdapter)msg.getAdapter()).writeByteArray(msg.getParser().formatMessage(msg));
		} else {
			throw new AdapterException(msg.getAdapter()+" must implement IByteArrayAdapter");
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
		parseMessage.setAdapter(adapter);
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
		parseMessage.setAdapter(adapter);
		return parseMessage;
	}
	public static Map<String,Object> parseJsonAsMap(String json){
		Type type = new TypeToken<Map<String, Object>>() {}.getType();
		return GSON.fromJson(json, type);
	}
	public static Object parseJson(String json, Type type){
		return GSON.fromJson(json, type);
	}
}
