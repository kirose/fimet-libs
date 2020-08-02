package com.fimet.usecase;

import java.util.ArrayList;
import java.util.List;

import com.fimet.exe.UseCaseResult;
import com.fimet.net.IConnectable;
import com.fimet.parser.IMessage;
import com.fimet.simulator.ISimulator;
import com.fimet.simulator.ISimulatorExtension;
import com.fimet.simulator.NullSimulatorExtension;
import com.google.gson.annotations.Expose;

public class UseCase implements IUseCase {
	private String name;
	private String authorization;
	private IMessage message;
	@Expose(serialize=false,deserialize=false)
	private IMessage response;
	@Expose(serialize=false,deserialize=false)
	private UseCaseResult result;
	@Expose(serialize=false,deserialize=false)
	private Object source;
	private List<ISimulator> simulators;
	private ISimulatorExtension simulatorExtension;
	public UseCase() {
		this.simulatorExtension = NullSimulatorExtension.INSTANCE;
		this.simulators = new ArrayList<>();
	}
	@Override
	public String getName() {
		return name;
	}
	@Override
	public String getAuthorization() {
		return authorization;
	}
	@Override
	public IMessage getMessage() {
		return message;
	}
	public void setAcquirer(ISimulator acquirer) {
		simulators.set(0, acquirer);
	}
	@Override
	public ISimulator getAcquirer() {
		return simulators != null && !simulators.isEmpty() ? simulators.get(0) : null;
	}
	@Override
	public List<ISimulator> getSimulators() {
		return simulators;
	}
	@Override
	public ISimulatorExtension getSimulatorExtension() {
		return simulatorExtension;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setMessage(IMessage message) {
		this.message = message;
	}
	public void addSimulator(ISimulator simulator) {
		simulators.add(simulator);
	}
	public void setSimulators(List<ISimulator> simulators) {
		this.simulators = simulators;
	}
	public void setAuthorization(String authorization) {
		this.authorization = authorization;
	}
	public void setSimulatorExtension(ISimulatorExtension simulatorExtension) {
		this.simulatorExtension = simulatorExtension != null ? simulatorExtension : NullSimulatorExtension.INSTANCE;
	}
	public String toString() {
		return name;
	}
	public IMessage getResponse() {
		return response;
	}
	public void setResponse(IMessage response) {
		this.response = response;
	}
	public UseCaseResult getResult() {
		return result;
	}
	public void setResult(UseCaseResult result) {
		this.result = result;
	}
	public Object getSource() {
		return source;
	}
	public void setSource(Object source) {
		this.source = source;
	}
	@Override
	public List<IConnectable> getConnectables() {
		List<IConnectable> connectables = new ArrayList<>();
		for (ISimulator s : simulators) {
			connectables.add(s);
		}
		return connectables;
	}
}
