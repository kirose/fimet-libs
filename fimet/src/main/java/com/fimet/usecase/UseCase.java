package com.fimet.usecase;

import java.util.ArrayList;
import java.util.List;

import com.fimet.iso8583.parser.Message;
import com.fimet.net.IConnectable;
import com.fimet.simulator.ISimulator;
import com.fimet.simulator.ISimulatorExtension;
import com.fimet.simulator.NullSimulatorExtension;
import com.fimet.usecase.exe.ExecutionResult;

public class UseCase implements IUseCase {
	private String name;
	private String authorization;
	private Message message;
	private Message response;
	private ExecutionResult result;
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
	public Message getMessage() {
		return message;
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
	public void setMessage(Message message) {
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
	public Message getResponse() {
		return response;
	}
	public void setResponse(Message response) {
		this.response = response;
	}
	public ExecutionResult getResult() {
		return result;
	}
	public void setResult(ExecutionResult result) {
		this.result = result;
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
