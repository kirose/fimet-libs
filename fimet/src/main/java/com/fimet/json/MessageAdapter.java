package com.fimet.json;

import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.fimet.FimetLogger;
import com.fimet.IAdapterManager;
import com.fimet.IParserManager;
import com.fimet.Manager;
import com.fimet.parser.Field;
import com.fimet.parser.IMessage;
import com.fimet.parser.Message;
import com.fimet.parser.ParserException;
import com.fimet.utils.data.ByteBuilder;

public class MessageAdapter extends TypeAdapter<Message>{
	private static IParserManager parserManager = Manager.get(IParserManager.class);
	private static IAdapterManager adapterManager = Manager.get(IAdapterManager.class);
	@Override
	public Message read(JsonReader in) throws IOException {
		Message msg = new Message();
		in.beginObject();
		String name;
		 
 		while (in.hasNext() && in.peek() == JsonToken.NAME) {
			name = in.nextName();
			if ("parser".equals(name)) {
				msg.setProperty(name, parserManager.getParser(in.nextString()));
			} else if ("adapter".equals(name)) {
				msg.setProperty(name, adapterManager.getAdapter(in.nextString()));
			} else if ("fields".equals(name)) {
				if (msg.getParser() == null) {
					throw new ParserException("message.parser must be declared before message.fields");
				}
				try {
					readFields(in, msg);
				} catch (Exception e) {
					FimetLogger.warning("Error Parsing message",e);
				}
			} else {
				msg.setProperty(name, in.nextString());
			}
		}
		in.endObject();
		if (msg.getParser() == null) {
			throw new ParserException("message.parser is required");
		}
		return msg;
	}
	private void readFields(JsonReader in, IMessage message) throws IOException{
		if (in.hasNext() && in.peek() == JsonToken.NULL) {
			in.nextNull();
		}
		in.beginObject();
		while (in.hasNext()) {
			readField(in, null, message);
		}
		in.endObject();
	}
	private void readField(JsonReader in, String idParent, IMessage message) throws IOException {
		if (!in.hasNext()) {
			return;
		}
		String key = in.nextName();
		String idField;
		if (in.hasNext()) {
			idField = idParent != null ? (idParent+"."+key) : key;
			if (in.peek() == JsonToken.STRING) {
				String value = in.nextString();
				message.setValue(idField, value);
			} else if (in.peek() == JsonToken.BEGIN_OBJECT) {
				message.setValue(idField, (byte[])null);
				in.beginObject();
				while (in.hasNext() && in.peek() != JsonToken.END_OBJECT) {
					readField(in, idField, message);
				}
				formatParent(message, idField);
				in.endObject();
			}
		}
	}
	private void formatParent(IMessage message, String idField) {
		try {
			ByteBuilder writer = new ByteBuilder();
			message.getParser().getFieldGroup().format(idField, message, writer);
			message.setValue(idField, writer.getBytes());
		} catch (Exception e) {
			FimetLogger.warning(MessageAdapter.class, "Cannot format field "+idField+": "+e.getMessage(),e);
		}
	}
	@Override
	public void write(JsonWriter out, Message msg) throws IOException {
		 out.beginObject();
		 for (Entry<String, Object> e : msg.getProperties().entrySet()) {
			 if (e.getValue() != null && ("parser".equals(e.getKey()) || "adapter".equals(e.getKey()))) {
				 out.name(e.getKey()).value(e.getValue().toString());
			 }
		}
		 writeFields(out, msg);
		 out.endObject();
	}
	private void writeFields(JsonWriter out, Message message) throws IOException{
 		out.name("fields");
 		out.beginObject();
 		List<Field> roots = message.getRootFields();
 		for (Field field : roots) {
			out.name(field.getKey());
			if (field.hasChildren()) {
				formatParent(out, field);
			} else {
				out.value(field.getValue());
			}
		}
 		out.endObject();
	}
	private void formatParent(JsonWriter out, Field parent) throws IOException {
		out.beginObject();
		for (Field field : parent.getChildren()) {
			out.name(field.getKey());
			if (field.hasChildren()) {
				formatParent(out, field);
			} else {
				out.value(field.getValue());
			}			
		}
		out.endObject();
	}
}
