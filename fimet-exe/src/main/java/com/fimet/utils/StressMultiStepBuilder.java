package com.fimet.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.LoggerFactory;import org.slf4j.Logger;

import com.fimet.ISimulatorManager;
import com.fimet.Manager;
import com.fimet.exe.SocketResult;
import com.fimet.exe.stress.NullStressMonitor;
import com.fimet.net.ISocket;
import com.fimet.parser.Message;
import com.fimet.simulator.IESimulator;
import com.fimet.simulator.ISimulator;
import com.fimet.stress.IStress;
import com.fimet.stress.IStressMonitor;
import com.fimet.stress.creator.CartesianCreator;
import com.fimet.usecase.UseCase;

public class StressMultiStepBuilder implements IStressMonitor {
	private static Logger logger = LoggerFactory.getLogger(StressMultiStepBuilder.class);
	static ISimulatorManager simulatorManager = Manager.getManager(ISimulatorManager.class);
	private List<ISimulator> simulators;
	private ConcurrentLinkedQueue<Step> steps = new ConcurrentLinkedQueue<Step>();
	private Map<File, IStepFileCreator> stepFileCreators;
	private File output;
	private IStressMonitor listener;
	private String name; 
	public StressMultiStepBuilder(String name) {
		this.name = name;
		simulators = new ArrayList<>();
		stepFileCreators = new HashMap<>();
		output = new File(Manager.getProperty("claspath.stress","stress/"));
		listener = NullStressMonitor.INSTANCE;
	}
	public StressMultiStepBuilder setExecutorListener(IStressMonitor listener) {
		this.listener = listener != null ? listener : NullStressMonitor.INSTANCE;
		return this;
	}
	public StressMultiStepBuilder connect(IESimulator ... simulators) {
		for (IESimulator simulator : simulators) {
			this.simulators.add(simulatorManager.getSimulator(simulator));
		}
		return this;
	}
	public StressMultiStepBuilder connect(IESimulator simulator) {
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
		StressBuilder sb = new StressBuilder(name+"-"+step)
    	.cycleTime(step.cycleTime)
    	.messagesPerCycle(step.numOfInjecion)
    	.monitor(this)
		.connect(simulators);
		for (Entry<File, IStepFileCreator> e : stepFileCreators.entrySet()) {
			UseCase useCase = (UseCase)UseCaseUtils.fromFile(e.getKey());
			ISimulator simulator = useCase.getAcquirer();
			ISocket socket = useCase.getAcquirer().getSocket();
			CartesianCreator creator = new CartesianCreator(new File(output,"stress-"+socket.getPort()+".txt"));
			useCase.getMessage().setProperty(Message.ADAPTER, socket.getAdapter());
			creator.setMessage(useCase.getMessage());
			File stressFile = e.getValue().create(step, creator);
			sb.addStressFile(simulator, stressFile);
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
		for (Entry<File, IStepFileCreator> e : stepFileCreators.entrySet()) {
			try {
				UseCase useCase = (UseCase)UseCaseUtils.fromFile(e.getKey());
				ISocket socket = useCase.getAcquirer().getSocket();
				File file = new File(output,"stress-"+socket.getPort()+".txt");
				if (file.exists() && file.isFile()) {
					file.delete();
				}
			} catch (Exception ex) {
				logger.error("Invalid use case "+e.getKey(), ex);
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
		public String toString() {
			return cycleTime+"x"+numOfInjecion+"["+panStart+","+panEnd+"]";
		}
	}
	@Override
	public void onInjectorFinishCycle(SocketResult result) {
		listener.onInjectorFinishCycle(result);
	}
	@Override
	public void onInjectorStart(SocketResult result) {
		listener.onInjectorStart(result);
	}
	@Override
	public void onInjectorFinish(SocketResult result) {
		listener.onInjectorFinish(result);
	}
	@Override
	public void onStressStart(IStress stress) {
		listener.onStressStart(stress);
	}
	@Override
	public void onStressFinish(IStress stress, List<SocketResult> results) {
		listener.onStressFinish(stress, results);
		executeNextStep();
	}
}
