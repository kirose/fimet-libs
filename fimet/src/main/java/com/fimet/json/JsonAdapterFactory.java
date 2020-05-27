package com.fimet.json;


import com.fimet.parser.IMessage;
import com.fimet.pojo.Field;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class JsonAdapterFactory implements TypeAdapterFactory {
	private static final JsonAdapterFactory INSTANCE = new JsonAdapterFactory();
	public static final Gson GSON = new GsonBuilder()
			.disableHtmlEscaping()
			.registerTypeAdapterFactory(INSTANCE)
			.create();
	public static final Gson GSON_PRETTY = new GsonBuilder()
			.setPrettyPrinting()
			.disableHtmlEscaping()
			.registerTypeAdapterFactory(INSTANCE)
			.create();
	
	private static final Type listFieldType = new TypeToken<List<Field>>() {}.getType();
	private static final Type iMessageType = new TypeToken<IMessage>() {}.getType();
	@SuppressWarnings("unchecked")
	public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
		if (type.getType().equals(iMessageType)) {
			return (TypeAdapter<T>)new MessageAdapter();
		} else if (type.getType().equals(listFieldType)) {
			return (TypeAdapter<T>)new ListFieldAdapter((TypeAdapter<List<Field>>)gson.getDelegateAdapter(this, type));
		} else {
			return gson.getDelegateAdapter(this, type);
		}
	}
	
}
