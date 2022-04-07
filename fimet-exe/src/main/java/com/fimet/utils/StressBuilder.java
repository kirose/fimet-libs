package com.fimet.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fimet.FimetException;
import com.fimet.IExecutorManager;
import com.fimet.ISimulatorManager;
import com.fimet.ISocketManager;
import com.fimet.Manager;
import com.fimet.exe.stress.NullStressMonitor;
import com.fimet.net.IConnectable;
import com.fimet.net.IMultiConnectable;
import com.fimet.net.IMultiConnectorListener;
import com.fimet.net.MultiConnector;
import com.fimet.simulator.IESimulator;
import com.fimet.simulator.ISimulator;
import com.fimet.stress.IStressMonitor;
import com.fimet.stress.Stress;
 
public class StressBuilder implements IMultiConnectorListener, IMultiConnectable {
	static ISimulatorManager simulatorManager = Manager.getManager(ISimulatorManager.class);
	static ISocketManager socketManager = Manager.getManager(ISocketManager.class);
	static IExecutorManager executorManager = Manager.getManager(IExecutorManager.class);
	
	private Stress stress;
	private IStressMonitor monitor;
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
	public StressBuilder connect(IESimulator ... simulators) {
		for (IESimulator psimulator : simulators) {
			ISimulator simulator = simulatorManager.getSimulator(psimulator);
			addConnectableSafe(simulator);
		}
		return this;
	}
	public StressBuilder connect(IESimulator psimulator) {
		ISimulator simulator = simulatorManager.getSimulator(psimulator);
		addConnectableSafe(simulator);
		return this;
	}
	public StressBuilder cycleTime(int cycleTimeInMiliseconds) {
		stress.setCycleTime(cycleTimeInMiliseconds);
		return this;
	}
	public StressBuilder messagesPerCycle(int messagesPerCycle) {
		stress.setMessagesPerCycle(messagesPerCycle);
		return this;
	}
	public StressBuilder addStressFile(ISimulator simulator, File stressFile) {
		addConnectableSafe(simulator);
		stress.getStressFiles().put(simulator, stressFile);
		return this;
	}
	public StressBuilder addStressFile(IESimulator pSimulator, File stressFile) {
		ISimulator simulator = simulatorManager.getSimulator(pSimulator);
		stress.getStressFiles().put(simulator, stressFile);
		addConnectableSafe(simulator);
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
	public StressBuilder monitor(IStressMonitor monitor) {
		this.monitor = monitor != null ? monitor : NullStressMonitor.INSTANCE;
		return this;
	}
	public void execute() {
		new MultiConnector(this).connectAsync(this);
	}
	@Override
	public void onConnectorConnectAll(IMultiConnectable connectable) {
		executorManager.setStressMonitor(monitor);
		executorManager.execute(stress);
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
