package com.fimet.exe;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

import com.fimet.FimetException;
import com.fimet.ISimulatorManager;
import com.fimet.ISocketManager;
import com.fimet.Manager;
import com.fimet.exe.stress.IInjector;
import com.fimet.exe.stress.IInjectorListener;
import com.fimet.exe.stress.NullStressExecutorListener;
import com.fimet.exe.stress.NullStressStore;
import com.fimet.simulator.ISimulator;
import com.fimet.socket.IConnectable;
import com.fimet.socket.ISocket;
import com.fimet.socket.MultiConnector;
import com.fimet.socket.IMultiConnectable;
import com.fimet.socket.MultiConnector.IMultiConnectorListener;
import com.fimet.stress.IStressExecutor;
import com.fimet.stress.IStressExecutorListener;
import com.fimet.stress.IStressStore;
import com.fimet.stress.Stress;

import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

public class StressExecutor
implements 
	IInjectorListener,
	IStressExecutor,
	IMultiConnectorListener {

	static ISocketManager socketManager = Manager.get(ISocketManager.class);
	static ISimulatorManager simulatorManager = Manager.get(ISimulatorManager.class);
	
	private IStressExecutorListener listener;
	private IExecutorListener executorListener;
	volatile private Map<ISimulator, StressInjector> injectors;
	volatile private Set<ISimulator> toComplete;
	volatile private IStressStore store;
	private Queue<Task> queue = new ConcurrentLinkedQueue<Task>();
	private Task executing;
	private StressResult result;
	public StressExecutor() {
		this.injectors = new HashMap<ISimulator, StressInjector>();
		this.toComplete = new HashSet<ISimulator>();
		this.listener = NullStressExecutorListener.INSTANCE;
		this.store = NullStressStore.INSTANCE;
	}
	@Override
	public void setStore(IStressStore store) {
		this.store = store != null ? store : NullStressStore.INSTANCE;
	}
	public void setListener(IStressExecutorListener listener) {
		this.listener = listener != null ? listener : NullStressExecutorListener.INSTANCE;
	}
	@Override
	public void execute(Task task) {
		if (task.getSource() instanceof Stress) {
			queue.add(task);
			checkNext();
		} else {
			throw new ExecutionException("Invalid execution type "+task.getSource());
		}
	}
	private void checkNext() {
		if (this.executing == null && !queue.isEmpty()) {
			this.executing = queue.poll();
			store.open(executing.getId());
			prepareExecution((Stress)executing.getSource());
		}
	}
	@Override
	public void setExecutorListener(IExecutorListener listener) {
		executorListener = listener!=null?listener:NullExecutorListener.INSTANCE;
	}
	@Override
	public void cancel() {
		if (executing != null) {
			for (Entry<ISimulator, StressInjector> e : injectors.entrySet()) {
				e.getValue().stopInjector();
			}
			injectors.clear();
			toComplete.clear();
			checkNext();
		}
	}
	private void prepareExecution(Stress stress) {
		result = new StressResult();
		result.setName(stress.getName());
		result.setStartTime(System.currentTimeMillis());
		new MultiConnector(stress).connectAsync(this);
	}
	@Override
	public void onConnectorConnect(IConnectable connectable) {}
	@Override
	public void onConnectorConnectAll(IMultiConnectable connectable) {
		doExecute((Stress)connectable);
	}
	@Override
	public void onConnectorTimeout(IMultiConnectable connectable) {
		throw new FimetException("Cannot connect connectables for stress "+connectable);
	}
	private void doExecute(Stress stress) {
		socketManager.setStore(null);
		simulatorManager.setStore(null);
		listener.onStressStart(stress);
		for (Map.Entry<ISimulator, File> e : stress.getStressFiles().entrySet()) {
			toComplete.add(e.getKey());
			StressInjector injector = new StressInjector(stress, e.getKey(), e.getValue());
			injector.setListener(StressExecutor.this);
			injectors.put(e.getKey(), injector);
			injector.startInjector();
		}
	}
	public void onExecutorFinishStress() {
		for (Entry<ISimulator, StressInjector> e : injectors.entrySet()) {
			ISocket socket = e.getKey().getSocket();
			e.getValue().getResult().numOfRead.set(socket.getNumOfRead()-e.getValue().getResult().initialReadSocket.get());
			e.getValue().getResult().numOfWrite.set(socket.getNumOfWrite()-e.getValue().getResult().initialWriteSocket.get());
		}
		List<InjectorResult> results = getInjectorResults();
		this.result.setFinishTime(System.currentTimeMillis());
		this.result.setInjectorResults(results);
		this.result.setNumOfInjectors(results.size());
		executing.result = this.result;

		listener.onStressFinish((Stress)executing.getSource(), results);
		executorListener.onTaskFinish(executing);

		store.close(executing.getId());
		executing = null;
		result = null;
		toComplete.clear();
		injectors.clear();
		checkNext();
	}
	public boolean hasFinish() {
		return toComplete.isEmpty();
	}
	@Override
	synchronized public void onInjectorStartInject(IInjector injector) {
		listener.onInjectorStart(injector.getResult());
	}
	@Override
	synchronized public void onInjectorFinishInject(IInjector injector) {
		ISocket socket = injector.getSocket();
		ISimulator simulator = injector.getSimulator();
		injector.getResult().numOfWrite.set(injector.getResult().getInjectorMessagesInjected());
		listener.onInjectorFinish(injector.getResult());
		toComplete.remove(simulator);
		InjectorResult ir = injector.getResult();
		ir.numOfApprovals.set(simulator.getNumOfApprovals()-ir.initialApprovalsSimulator.get());
		ir.numOfRead.set(socket.getNumOfRead()-ir.initialReadSocket.get());
		ir.hasFinished.set(true);
		ir.startTime = ir.injectorStartTime;
		ir.finishTime = System.currentTimeMillis();		
		store.storeGlobalResults(ir);
		if (toComplete.isEmpty()) {
			onExecutorFinishStress();
		}
	}
	@Override
	public void onInjectorStartCycle(IInjector injector) {}
	@Override
	public void onInjectorFinishCycle(IInjector injector) {
		InjectorResult ir = injector.getResult();
		ISocket socket = injector.getSocket();
		ISimulator simulator = injector.getSimulator();
		ir.numOfCycle.incrementAndGet();
		long numOfWrite = socket.getNumOfWrite()-ir.initialWriteSocket.get();
		ir.numOfWriteCycle.set(numOfWrite-ir.numOfWrite.get());
		ir.numOfWrite.set(numOfWrite);
		long numOfRead = socket.getNumOfRead()-ir.initialReadSocket.get();
		ir.numOfReadCycle.set(numOfRead-ir.numOfRead.get());
		ir.numOfRead.set(numOfRead);
		long numOfApprovals = simulator.getNumOfApprovals()-ir.initialApprovalsSimulator.get();
		ir.numOfApprovalsCycle.set(numOfApprovals-ir.numOfApprovals.get());
		ir.numOfApprovals.set(numOfApprovals);
		store.storeCycleResults(injector.getResult());
		listener.onInjectorFinishCycle(injector.getResult());
	}
	public List<InjectorResult> getInjectorResults() {
		List<InjectorResult> results = new ArrayList<>();
		for (Entry<ISimulator, StressInjector> e : injectors.entrySet()) {
			results.add(e.getValue().getResult());
		}
		return results;
	}
}
