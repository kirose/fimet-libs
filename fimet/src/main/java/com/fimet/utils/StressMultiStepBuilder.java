package com.fimet.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.fimet.ISimulatorManager;
import com.fimet.Manager;
import com.fimet.commons.FimetLogger;
import com.fimet.net.ISocket;
import com.fimet.simulator.ISimulator;
import com.fimet.simulator.PSimulator;
import com.fimet.stress.creator.CartesianCreator;
import com.fimet.stress.exe.IExecutor;
import com.fimet.stress.exe.IExecutorListener;
import com.fimet.stress.exe.IStoreResults;
import com.fimet.stress.exe.InjectorResult;
import com.fimet.stress.exe.NullExecutorListener;
import com.fimet.stress.exe.NullStoreResults;
import com.fimet.usecase.UseCase;

public class StressMultiStepBuilder implements IExecutorListener {
	static ISimulatorManager simulatorManager = Manager.get(ISimulatorManager.class);
	private List<ISimulator> simulators;
	private ConcurrentLinkedQueue<Step> steps = new ConcurrentLinkedQueue<Step>();
	private Map<File, IStepFileCreator> stepFileCreators;
	private IStoreResults store;
	private File output;
	private IExecutorListener listener;
	public StressMultiStepBuilder() {
		simulators = new ArrayList<>();
		store = NullStoreResults.INSTANCE;
		stepFileCreators = new HashMap<>();
		output = new File("stress/");
		listener = NullExecutorListener.INSTANCE;
	}
	public StressMultiStepBuilder setStore(IStoreResults store) {
		this.store = store != null ? store : NullStoreResults.INSTANCE;
		return this;
	}
	public StressMultiStepBuilder setExecutorListener(IExecutorListener listener) {
		this.listener = listener != null ? listener : NullExecutorListener.INSTANCE;
		return this;
	}
	public StressMultiStepBuilder connect(PSimulator ... simulators) {
		for (PSimulator simulator : simulators) {
			this.simulators.add(simulatorManager.getSimulator(simulator));
		}
		return this;
	}
	public StressMultiStepBuilder connect(PSimulator simulator) {
		simulators.add(simulatorManager.getSimulator(simulator));
		return this;
	}
	public StressMultiStepBuilder connect(List<ISimulator> simulators) {
		for (ISimulator simulator : simulators) {
			simulators.add(simulator);
		}
		return this;
	}
	public StressMultiStepBuilder connect(ISimulator ... simulators) {
		for (ISimulator simulator : simulators) {
			this.simulators.add(simulator);
		}
		return this;
	}
	public StressMultiStepBuilder addFileCreator(String useCasePath, IStepFileCreator fileCreator) {
		stepFileCreators.put(new File(useCasePath), fileCreator);
		return this;
	}
	public StressMultiStepBuilder addFileCreator(File useCase, IStepFileCreator fileCreator) {
		stepFileCreators.put(useCase, fileCreator);
		return this;
	}
	private void executeStep(Step step) {
		StressBuilder sb = new StressBuilder()
    	.setCycleTime(step.cycleTime)
    	.setMessagesPerCycle(step.numOfInjecion)
    	.setStoreResults(store)
    	.setExecutorListener(this)
		.connect(simulators);
		for (Entry<File, IStepFileCreator> e : stepFileCreators.entrySet()) {
			UseCase useCase = UseCaseUtils.parseForExecution(e.getKey());
			ISocket socket = useCase.getAcquirer().getSocket();
			CartesianCreator creator = new CartesianCreator(new File(output,"stress-"+socket.getPort()+".txt"));
			useCase.getMessage().setAdapter(socket.getAdapter());
			creator.setMessage(useCase.getMessage());
			File stressFile = e.getValue().create(step, creator);
			sb.addStressFile(socket, stressFile);
			List<ISimulator> simulators = useCase.getSimulators();
			for (int i = 1; i < simulators.size(); i++) {
				sb.connect(simulators.get(i));
			}
		}
		sb.execute();
	}
	private void executeNextStep() {
		if (steps.isEmpty()) {
			onMultiStepFinish();
		} else {
			Step step = steps.poll();
			executeStep(step);
		}
	}
	public void execute() {
		if (steps.isEmpty()) {
			onMultiStepFinish();
		} else {
			executeNextStep();
		}
	}
	private void onMultiStepFinish() {
		store.close();
		for (Entry<File, IStepFileCreator> e : stepFileCreators.entrySet()) {
			try {
				UseCase useCase = UseCaseUtils.parseForExecution(e.getKey());
				ISocket socket = useCase.getAcquirer().getSocket();
				File file = new File(output,"stress-"+socket.getPort()+".txt");
				if (file.exists() && file.isFile()) {
					file.delete();
				}
			} catch (Exception ex) {
				FimetLogger.error(StressMultiStepBuilder.class, "Invalid use case "+e.getKey(), ex);
			}
		}
	} 
	public StressMultiStepBuilder addStep(Step step) {
		steps.add(step);
		return this;
	}
	public static interface IStepFileCreator {
		File create(Step step, CartesianCreator creator);
	}
	public static class Step {
		int cycleTime;
		int numOfInjecion;
		int panStart;
		int panEnd;
		public Step(int cycleTime, int numOfInjecion, int panStart, int panEnd) {
			super();
			this.cycleTime = cycleTime;
			this.numOfInjecion = numOfInjecion;
			this.panStart = panStart;
			this.panEnd = panEnd;
		}
		public int getCycleTime() {
			return cycleTime;
		}
		public int getNumOfInjecion() {
			return numOfInjecion;
		}
		public int getPanStart() {
			return panStart;
		}
		public int getPanEnd() {
			return panEnd;
		}
	}
	@Override
	public void onInjectorFinishCycle(InjectorResult result) {
		listener.onInjectorFinishCycle(result);
	}
	@Override
	public void onInjectorStart(InjectorResult result) {
		listener.onInjectorStart(result);
	}
	@Override
	public void onInjectorFinish(InjectorResult result) {
		listener.onInjectorFinish(result);
	}
	@Override
	public void onExecutorStart(IExecutor executor) {
		listener.onExecutorStart(executor);
	}
	@Override
	public void onExecutorFinish(IExecutor executor) {
		listener.onExecutorFinish(executor);
		executeNextStep();
	}
}
