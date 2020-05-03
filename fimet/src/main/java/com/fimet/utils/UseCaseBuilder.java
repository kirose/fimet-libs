package com.fimet.utils;

import com.fimet.ISimulatorManager;
import com.fimet.IUseCaseManager;
import com.fimet.Manager;
import com.fimet.iso8583.parser.Message;
import com.fimet.simulator.ISimulator;
import com.fimet.simulator.ISimulatorExtension;
import com.fimet.simulator.NullSimulatorExtension;
import com.fimet.simulator.PSimulator;
import com.fimet.usecase.UseCase;

public class UseCaseBuilder {
	static ISimulatorManager simulatorManager = Manager.get(ISimulatorManager.class);
	static IUseCaseManager useCaseManager = Manager.get(IUseCaseManager.class);
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
		useCase.setMessage(new Message("0200","ISO858300000",simulator.getParser()));		
	}
	public UseCaseBuilder setAuthorization(String authorization) {
		useCase.setAuthorization(authorization);
		return this;
	}
	public UseCaseBuilder setMessageMti(String mti) {
		useCase.getMessage().setMti(mti);
		return this;
	}
	public UseCaseBuilder setMessageHeader(String header) {
		useCase.getMessage().setHeader(header);
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
		useCaseManager.execute(useCase);
		return this;
	}
}
