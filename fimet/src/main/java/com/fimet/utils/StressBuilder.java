package com.fimet.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fimet.ISimulatorManager;
import com.fimet.ISocketManager;
import com.fimet.Manager;
import com.fimet.commons.exception.FimetException;
import com.fimet.net.IConnectable;
import com.fimet.net.ISocket;
import com.fimet.net.MultiConnector;
import com.fimet.net.PSocket;
import com.fimet.net.MultiConnector.IMultiConnectorListener;
import com.fimet.net.MultiConnector.IMultiConnectable;
import com.fimet.simulator.ISimulator;
import com.fimet.simulator.PSimulator;
import com.fimet.stress.Stress;
import com.fimet.stress.exe.IExecutorListener;
import com.fimet.stress.exe.IStoreResults;
import com.fimet.stress.exe.StressExecutor;

public class StressBuilder implements IMultiConnectorListener, IMultiConnectable {
	static ISimulatorManager simulatorManager = Manager.get(ISimulatorManager.class);
	static ISocketManager socketManager = Manager.get(ISocketManager.class);
	private Stress stress;
	private StressExecutor executor;
	private List<IConnectable> connectables;
	public StressBuilder() {
		stress = new Stress();
		stress.setName("Stress Unkown");
		stress.setStressFiles(new HashMap<>());
		executor = new StressExecutor(stress);
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
	public StressBuilder setStoreResults(IStoreResults store) {
		executor.setStoreResults(store);
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
		IConnectable found = null;
		if (c instanceof ISocket) {
			found = findConnectableAsociated((ISocket)c);
		} else if (c instanceof ISocket) {
			found = findConnectableAsociated((ISimulator)c);
		}
		if (found == null) {
			connectables.add(c);
		}
	}
	private IConnectable findConnectableAsociated(ISimulator s) {
		if (!connectables.isEmpty()) {
			for (IConnectable c : connectables) {
				if (c instanceof ISimulator && c == s) {
					return c;
				} else if (c instanceof ISocket && c == s.getSocket()) {
					return c;
				}
			}
		}
		return null;
	}
	private IConnectable findConnectableAsociated(ISocket s) {
		if (!connectables.isEmpty()) {
			for (IConnectable c : connectables) {
				if (c instanceof ISimulator && ((ISimulator) c).getSocket() == s) {
					return c;
				} else if (c instanceof ISocket && c == s) {
					return c;
				}
			}
		}
		return null;
	}
	public Stress build() {
		return stress;
	}
	public StressBuilder setExecutorListener(IExecutorListener listener) {
		executor.setListener(listener);
		return this;
	}
	public void execute() {
		new MultiConnector(this).connectAsync();
	}
	@Override
	public void onConnectorConnectAll(IMultiConnectable connectable) {
		executor.execute();
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
