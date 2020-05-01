package com.fimet.json.adapter;


import com.fimet.entity.sqlite.pojo.Field;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class JsonAdapterFactory implements TypeAdapterFactory {
	
	public static final Gson GSON = new GsonBuilder()
			.setPrettyPrinting()
			.disableHtmlEscaping()
			.registerTypeAdapterFactory(new JsonAdapterFactory())
			.create();

	private static final Type listFieldType = new TypeToken<List<Field>>() {}.getType();
	@SuppressWarnings("unchecked")
	public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
		if (type.getType().equals(listFieldType)) {
			return (TypeAdapter<T>)new ListFieldAdapter((TypeAdapter<List<Field>>)gson.getDelegateAdapter(this, type));
		} else {
			return gson.getDelegateAdapter(this, type);
		}
	}
	
}
