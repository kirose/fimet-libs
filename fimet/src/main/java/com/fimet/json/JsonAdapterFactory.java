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
	
	private static final Type iSimulatorType = new TypeToken<ISimulator>() {}.getType();
	private static final Type iMessageType = new TypeToken<IMessage>() {}.getType();
	private static final Type iUseCaseType = new TypeToken<IUseCase>() {}.getType();
	private static final Type messageJsonType = new TypeToken<MessageJson>() {}.getType();
	
	@SuppressWarnings("unchecked")
	public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
		if (type.getType().equals(iUseCaseType)) {
			return (TypeAdapter<T>)new UseCaseAdapter((TypeAdapter<IUseCase>)gson.getDelegateAdapter(this, type));
		} else if (type.getType().equals(messageJsonType)) {
			return (TypeAdapter<T>)new MessageJsonAdapter();
		} else if (type.getType().equals(iMessageType)) {
			return (TypeAdapter<T>)new MessageAdapter();
		} else if (type.getType().equals(iSimulatorType)) {
			return (TypeAdapter<T>)new SimulatorAdapter((TypeAdapter<ISimulator>)gson.getDelegateAdapter(this, type));
		} else {
			return gson.getDelegateAdapter(this, type);
		}
	}
	
}
