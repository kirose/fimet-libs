package com.fimet.utils;

import com.fimet.IAdapterManager;
import com.fimet.IParserManager;
import com.fimet.Manager;
import com.fimet.parser.IMessage;
import com.fimet.parser.IParser;
import com.fimet.parser.Message;
import com.fimet.parser.IAdapter;
import com.fimet.usecase.IUseCase;

public class MessageBuilder {
	static IAdapterManager adapterManager = Manager.get(IAdapterManager.class);
	static IParserManager parserManager = Manager.get(IParserManager.class);
	IMessage message;
	public MessageBuilder() {
		this.message = new Message();
	}
	public MessageBuilder(Message message) {
		this.message = message;
	}
	public MessageBuilder(IUseCase useCase) {
		this.message = useCase.getMessage();
	}
	public MessageBuilder setMti(String mti) {
		message.setProperty(Message.MTI, mti);
		return this;
	}
	public MessageBuilder setHeader(String header) {
		message.setProperty(Message.HEADER, header);
		return this;
	}
	public MessageBuilder setParser(int idParser) {
		message.setParser(parserManager.getParser(idParser));
		return this;
	}
	public MessageBuilder setParser(String parser) {
		message.setParser(parserManager.getParser(parser));
		return this;
	}
	public MessageBuilder setParser(IParser parser) {
		message.setParser(parser);
		return this;
	}
	
	public MessageBuilder setAdapter(int idAdapter) {
		message.setProperty(Message.ADAPTER, adapterManager.getAdapter(idAdapter));
		return this;
	}
	public MessageBuilder setAdapter(String adapter) {
		message.setProperty(Message.ADAPTER, adapterManager.getAdapter(adapter));
		return this;
	}
	public MessageBuilder setAdapter(IAdapter adapter) {
		message.setProperty(Message.ADAPTER, adapter);
		return this;
	}
	public MessageBuilder setValue(int idField, String value) {
		message.setValue(idField, value);
		return this;
	}
	public MessageBuilder setValue(String idField, String value) {
		message.setValue(idField, value);
		return this;
	}
	public MessageBuilder removeValue(int idField) {
		message.remove(idField);
		message.remove(idField);
		return this;
	}
	public MessageBuilder removeValue(String idField) {
		message.remove(idField);
		return this;
	}
	public IMessage build() {
		return message;
	}
}
