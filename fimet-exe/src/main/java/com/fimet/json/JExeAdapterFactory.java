package com.fimet.json;


import com.fimet.parser.IMessage;
import com.fimet.simulator.ISimulator;
import com.fimet.usecase.IUseCase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class JExeAdapterFactory implements TypeAdapterFactory {
	public static final JExeAdapterFactory INSTANCE = new JExeAdapterFactory();
	public static final Gson GSON = new GsonBuilder()
			.disableHtmlEscaping()
			.registerTypeAdapterFactory(JExeAdapterFactory.INSTANCE)
			.create();
	public static final Gson GSON_PRETTY = new GsonBuilder()
			.setPrettyPrinting()
			.disableHtmlEscaping()
			.registerTypeAdapterFactory(JExeAdapterFactory.INSTANCE)
			.create();

	private static final Type iSimulatorType = new TypeToken<ISimulator>() {}.getType();
	private static final Type iMessageType = new TypeToken<IMessage>() {}.getType();
	private static final Type iUseCaseType = new TypeToken<IUseCase>() {}.getType();
	private static final Type jUseCaseMapType = new TypeToken<JUseCase>() {}.getType();
	private static final Type jUseCaseJMsgType = new TypeToken<JUseCaseJMsg>() {}.getType();
	private static final Type jStressFileBuilderType = new TypeToken<JStressFileBuilder>() {}.getType();
	private static final Type messageMapType = new TypeToken<JMessageMap>() {}.getType();
	private static final Type messageTreeType = new TypeToken<JMessageTree>() {}.getType();
	
	@SuppressWarnings("unchecked")
	public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
		if (type.getType().equals(iUseCaseType)) {
			return (TypeAdapter<T>)new IUseCaseAdapter((TypeAdapter<IUseCase>)gson.getDelegateAdapter(this, type));
		} else if (type.getType().equals(jUseCaseMapType)) {
			return (TypeAdapter<T>)new JUseCaseAdapter((TypeAdapter<JUseCase>)gson.getDelegateAdapter(this, type));
		} else if (type.getType().equals(jUseCaseJMsgType)) {
			return (TypeAdapter<T>)new JUseCaseJMsgAdapter((TypeAdapter<JUseCaseJMsg>)gson.getDelegateAdapter(this, type));
		} else if (type.getType().equals(messageMapType)) {
			return (TypeAdapter<T>)new JMessageMapAdapter();
		} else if (type.getType().equals(messageTreeType)) {
			return (TypeAdapter<T>)new JMessageTreeAdapter();
		} else if (type.getType().equals(iMessageType)) {
			return (TypeAdapter<T>)new IMessageAdapter();
		} else if (type.getType().equals(iSimulatorType)) {
			return (TypeAdapter<T>)new ISimulatorAdapter((TypeAdapter<ISimulator>)gson.getDelegateAdapter(this, type));
		} else if (type.getType().equals(jStressFileBuilderType)) {
			return (TypeAdapter<T>)new JStressFileBuilderAdapter((TypeAdapter<JStressFileBuilder>)gson.getDelegateAdapter(this, type));
		} else {
			return gson.getDelegateAdapter(this, type);
		}
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
