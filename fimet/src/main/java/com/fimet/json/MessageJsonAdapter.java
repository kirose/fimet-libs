package com.fimet.json;

import java.io.IOException;
import java.util.Map.Entry;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.fimet.FimetLogger;

public class MessageJsonAdapter extends TypeAdapter<MessageJson>{
	@Override
	public MessageJson read(JsonReader in) throws IOException {
		MessageJson msg = new MessageJson();
		in.beginObject();
		String name;
		 
 		while (in.hasNext() && in.peek() == JsonToken.NAME) {
			name = in.nextName();
			if ("fields".equals(name)) {
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
		return msg;
	}
	private void readFields(JsonReader in, MessageJson message) throws IOException{
		if (in.hasNext() && in.peek() == JsonToken.NULL) {
			in.nextNull();
		}
		in.beginObject();
		while (in.hasNext()) {
			readField(in, null, message);
		}
		in.endObject();
	}
	private void readField(JsonReader in, String idParent, MessageJson message) throws IOException {
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
				in.beginObject();
				while (in.hasNext() && in.peek() != JsonToken.END_OBJECT) {
					readField(in, idField, message);
				}
				in.endObject();
			}
		}
	}
	@Override
	public void write(JsonWriter out, MessageJson msg) throws IOException {
		if (msg != null) {
			out.beginObject();
			for (Entry<String, String> e : msg.getProperties().entrySet()) {
				 if (e.getValue() != null && ("parser".equals(e.getKey()) || "adapter".equals(e.getKey()))) {
					 out.name(e.getKey()).value(e.getValue().toString());
				 }
			}
			writeFields(out, msg);
			out.endObject();
		} else {
			out.nullValue();
		}
	}
	private void writeFields(JsonWriter out, MessageJson message) throws IOException{
 		out.name("fields");
 		out.beginObject();
 		for (Entry<String, String> e : message.getFields().entrySet()) {
 			if (e.getValue() != null) {
				out.name(e.getKey());
				out.value(e.getValue());
 			}
		}
 		out.endObject();
	}
}
