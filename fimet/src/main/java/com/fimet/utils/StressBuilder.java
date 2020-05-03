package com.fimet.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fimet.IExecutorManager;
import com.fimet.ISimulatorManager;
import com.fimet.ISocketManager;
import com.fimet.Manager;
import com.fimet.commons.exception.FimetException;
import com.fimet.exe.IStressExecutorListener;
import com.fimet.net.IConnectable;
import com.fimet.net.ISocket;
import com.fimet.net.MultiConnector;
import com.fimet.net.PSocket;
import com.fimet.net.MultiConnector.IMultiConnectorListener;
import com.fimet.net.MultiConnector.IMultiConnectable;
import com.fimet.simulator.ISimulator;
import com.fimet.simulator.PSimulator;
import com.fimet.stress.Stress;
import com.fimet.stress.exe.NullStressExecutorListener;

public class StressBuilder implements IMultiConnectorListener, IMultiConnectable {
	static ISimulatorManager simulatorManager = Manager.get(ISimulatorManager.class);
	static ISocketManager socketManager = Manager.get(ISocketManager.class);
	static IExecutorManager executorManager = Manager.get(IExecutorManager.class);
	
	private Stress stress;
	private IStressExecutorListener listener;
	private List<IConnectable> connectables;
	public StressBuilder(String name) {
		stress = new Stress();
		stress.setName(name);
		stress.setStressFiles(new HashMap<>());
		connectables = new ArrayList<>();
	}
	public StressBuilder connect(List<ISimulator> simulators) {
		for (ISimulator simulator : simulators) {
			addConnectableSafe(simulator);
		}
		return this;
	}
	public StressBuilder connect(IConnectable ...conectables) {
		for (IConnectable c : conectables) {
			addConnectableSafe(c);
		}
		return this;
	}
	public StressBuilder connect(IMultiConnectable ...mconectables) {
		for (IMultiConnectable m : mconectables) {
			for (IConnectable c : m.getConnectables()) {
				addConnectableSafe(c);
			}
		}
		return this;
	}
	public StressBuilder connect(ISimulator ... simulators) {
		for (ISimulator simulator : simulators) {
			addConnectableSafe(simulator);
		}
		return this;
	}
	public StressBuilder connect(PSimulator ... simulators) {
		for (PSimulator psimulator : simulators) {
			ISimulator simulator = simulatorManager.getSimulator(psimulator);
			addConnectableSafe(simulator);
		}
		return this;
	}
	public StressBuilder connect(PSimulator psimulator) {
		ISimulator simulator = simulatorManager.getSimulator(psimulator);
		addConnectableSafe(simulator);
		return this;
	}
	public StressBuilder setCycleTime(int cycleTime) {
		stress.setCycleTime(cycleTime);
		return this;
	}
	public StressBuilder setMessagesPerCycle(int messagesPerCycle) {
		stress.setMessagesPerCycle(messagesPerCycle);
		return this;
	}
	public StressBuilder addStressFile(ISocket socket, File stressFile) {
		addConnectableSafe(socket);
		stress.getStressFiles().put(socket, stressFile);
		return this;
	}
	public StressBuilder addStressFile(PSocket pSocket, File stressFile) {
		ISocket socket = socketManager.getSocket(pSocket);
		stress.getStressFiles().put(socket, stressFile);
		addConnectableSafe(socket);
		return this;
	}
	private void addConnectableSafe(IConnectable c) {
		if (c instanceof ISimulator) {
			c = ((ISimulator)c).getSocket();
		}
		if (!connectables.contains(c)) {
			connectables.add(c);
		}
	}
	public Stress build() {
		return stress;
	}
	public StressBuilder setExecutorListener(IStressExecutorListener listener) {
		this.listener = listener != null ? listener : NullStressExecutorListener.INSTANCE;
		return this;
	}
	public void execute() {
		new MultiConnector(this).connectAsync(this);
	}
	@Override
	public void onConnectorConnectAll(IMultiConnectable connectable) {
		executorManager.execute(stress, listener);
	}
	@Override
	public List<IConnectable> getConnectables() {
		return connectables;
	}
	@Override
	public void onConnectorConnect(IConnectable simulator) {}
	@Override
	public void onConnectorTimeout(IMultiConnectable connectable) {
		throw new FimetException("Cannot connect simulators for stress "+stress.getName());
	}
}
