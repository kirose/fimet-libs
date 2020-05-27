package com.fimet.utils;

import com.fimet.IExecutorManager;
import com.fimet.ISimulatorManager;
import com.fimet.Manager;
import com.fimet.parser.Message;
import com.fimet.simulator.ISimulator;
import com.fimet.simulator.ISimulatorExtension;
import com.fimet.simulator.NullSimulatorExtension;
import com.fimet.simulator.PSimulator;
import com.fimet.usecase.UseCase;

public class UseCaseBuilder {
	static ISimulatorManager simulatorManager = Manager.get(ISimulatorManager.class);
	static IExecutorManager executorManager = Manager.get(IExecutorManager.class);
	UseCase useCase;
	public UseCaseBuilder(String name, PSimulator acquirer) {
		this(name, simulatorManager.getSimulator(acquirer));
	}
	public UseCaseBuilder(String name, String idSimulator) {
		this(name, simulatorManager.getSimulator(idSimulator));
	}
	public UseCaseBuilder(String name, ISimulator simulator) {
		useCase = new UseCase();
		useCase.setName(name);
		useCase.addSimulator(simulator);
		Message m = new Message();
		m.setProperty(Message.MTI, "0200");
		m.setProperty(Message.HEADER, "ISO858300000");
		m.setProperty(Message.PARSER, simulator.getParser());
		useCase.setMessage(m);		
	}
	public UseCaseBuilder setAuthorization(String authorization) {
		useCase.setAuthorization(authorization);
		return this;
	}
	public UseCaseBuilder setMessageMti(String mti) {
		useCase.getMessage().setProperty(Message.MTI, mti);
		return this;
	}
	public UseCaseBuilder setMessageHeader(String header) {
		useCase.getMessage().setProperty(Message.HEADER, header);
		return this;
	}
	public UseCaseBuilder setMessage(Message message) {
		useCase.setMessage(message);
		return this;
	}
	public UseCaseBuilder setMessageValue(int idField, String value) {
		useCase.getMessage().setValue(idField, value);
		return this;
	}
	public UseCaseBuilder setMessageValue(String idField, String value) {
		useCase.getMessage().setValue(idField, value);
		return this;
	}
	public UseCaseBuilder removeMessageValue(int idField) {
		useCase.getMessage().remove(idField);
		useCase.getMessage().remove(idField);
		return this;
	}
	public UseCaseBuilder removeMessageValue(String idField) {
		useCase.getMessage().remove(idField);
		return this;
	}
	public UseCaseBuilder addConnection(PSimulator simulator) {
		useCase.addSimulator(simulatorManager.getSimulator(simulator));
		return this;
	}
	public UseCaseBuilder setSimulatorExtension(ISimulatorExtension extension) {
		useCase.setSimulatorExtension(extension != null ? extension : NullSimulatorExtension.INSTANCE);
		return this;
	}
	public UseCase build() {
		return useCase;
	}
	public UseCaseBuilder execute() {
		executorManager.execute(useCase);
		return this;
	}
}
