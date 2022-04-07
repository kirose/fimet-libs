package com.fimet.utils;

import com.fimet.IExecutorManager;
import com.fimet.ISimulatorManager;
import com.fimet.Manager;
import com.fimet.parser.Message;
import com.fimet.simulator.IESimulator;
import com.fimet.simulator.ISimulator;
import com.fimet.simulator.ISimulatorExtension;
import com.fimet.simulator.NullSimulatorExtension;
import com.fimet.usecase.UseCase;

public class UseCaseBuilder {
	static ISimulatorManager simulatorManager = Manager.getManager(ISimulatorManager.class);
	static IExecutorManager executorManager = Manager.getManager(IExecutorManager.class);
	UseCase useCase;
	public UseCaseBuilder(String name, IESimulator acquirer) {
		this(name, simulatorManager.getSimulator(acquirer));
	}
	public UseCaseBuilder(String name, String simulator) {
		this(name, simulatorManager.getSimulator(simulator));
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
	public UseCaseBuilder authorization(String authorization) {
		useCase.setAuthorization(authorization);
		return this;
	}
	public UseCaseBuilder mti(String mti) {
		useCase.getMessage().setProperty(Message.MTI, mti);
		return this;
	}
	public UseCaseBuilder header(String header) {
		useCase.getMessage().setProperty(Message.HEADER, header);
		return this;
	}
	public UseCaseBuilder message(Message message) {
		useCase.setMessage(message);
		return this;
	}
	public UseCaseBuilder value(int idField, String value) {
		useCase.getMessage().setValue(idField, value);
		return this;
	}
	public UseCaseBuilder value(String idField, String value) {
		useCase.getMessage().setValue(idField, value);
		return this;
	}
	public UseCaseBuilder remove(int idField) {
		useCase.getMessage().remove(idField);
		useCase.getMessage().remove(idField);
		return this;
	}
	public UseCaseBuilder remove(String idField) {
		useCase.getMessage().remove(idField);
		return this;
	}
	public UseCaseBuilder addSimulator(ISimulator simulator) {
		useCase.addSimulator(simulator);
		return this;
	}
	public UseCaseBuilder addSimulator(IESimulator simulator) {
		useCase.addSimulator(simulatorManager.getSimulator(simulator));
		return this;
	}
	public UseCaseBuilder simulatorExtension(ISimulatorExtension extension) {
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
