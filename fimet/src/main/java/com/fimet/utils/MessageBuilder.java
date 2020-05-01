package com.fimet.utils;

import com.fimet.IAdapterManager;
import com.fimet.IParserManager;
import com.fimet.Manager;
import com.fimet.adapter.IAdapter;
import com.fimet.iso8583.parser.IParser;
import com.fimet.iso8583.parser.Message;
import com.fimet.usecase.UseCase;
import com.fimet.usecase.json.UseCaseJson;

public class MessageBuilder {
	static IAdapterManager adapterManager = Manager.get(IAdapterManager.class);
	static IParserManager parserManager = Manager.get(IParserManager.class);
	Message message;
	public MessageBuilder() {
		this.message = new Message();
	}
	public MessageBuilder(Message message) {
		this.message = message;
	}
	public MessageBuilder(UseCase useCase) {
		this.message = useCase.getMessage();
	}
	public MessageBuilder(String useCasePath) {
		UseCaseJson useCaseJson = com.fimet.utils.UseCaseUtils.parseJson(new java.io.File(useCasePath));
		this.message = useCaseJson.getMessage();
	}
	public MessageBuilder setMti(String mti) {
		message.setMti(mti);
		return this;
	}
	public MessageBuilder setHeader(String header) {
		message.setHeader(header);
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
		message.setAdapter(adapterManager.getAdapter(idAdapter));
		return this;
	}
	public MessageBuilder setAdapter(String adapter) {
		message.setAdapter(adapterManager.getAdapter(adapter));
		return this;
	}
	public MessageBuilder setAdapter(IAdapter adapter) {
		message.setAdapter(adapter);
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
	public Message build() {
		return message;
	}
}
