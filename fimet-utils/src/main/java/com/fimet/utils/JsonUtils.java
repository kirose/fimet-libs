package com.fimet.utils;

import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public final class JsonUtils {
	public static final Gson GSON = new GsonBuilder()
			.disableHtmlEscaping()
			.create();
	public static final Gson GSON_PRETTY = new GsonBuilder()
			.setPrettyPrinting()
			.disableHtmlEscaping()
			.create();
	private JsonUtils() {
	}
	public static <T>T fromJson(String json, Class<T> clazz) {
		return GSON.fromJson(json, clazz);
	}
	public static <T> T fromJson(String json, Type typeOfT) {
		return GSON.fromJson(json, typeOfT);
	}
	public static String toJson(Object object) {
		return GSON.toJson(object);
	}
	public static String toPrettyJson(Object object) {
		return GSON_PRETTY.toJson(object);
	}
	public static Map<String,Object> parseJsonAsMap(String json){
		Type type = new TypeToken<Map<String, Object>>() {}.getType();
		return GSON_PRETTY.fromJson(json, type);
	}
}
