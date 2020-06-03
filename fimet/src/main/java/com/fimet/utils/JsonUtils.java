package com.fimet.utils;

import static com.fimet.json.JsonAdapterFactory.GSON;
import static com.fimet.json.JsonAdapterFactory.GSON_PRETTY;

import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.reflect.TypeToken;


/**
 * 
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 *
 */
public final class JsonUtils {
	private JsonUtils() {}
	public static <T>T fromJson(String json, Class<T> clazz) {
		return GSON.fromJson(json, clazz);
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
