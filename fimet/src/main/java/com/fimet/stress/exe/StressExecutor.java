package com.fimet.stress.exe;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

import com.fimet.commons.exception.FimetException;
import com.fimet.exe.IStressExecutor;
import com.fimet.exe.IStressExecutorListener;
import com.fimet.exe.IStressStore;
import com.fimet.net.IConnectable;
import com.fimet.net.ISocket;
import com.fimet.net.MultiConnector;
import com.fimet.net.MultiConnector.IMultiConnectable;
import com.fimet.net.MultiConnector.IMultiConnectorListener;
import com.fimet.stress.IStress;
import com.fimet.stress.Stress;

import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

public class StressExecutor
implements 
	IInjectorListener,
	IStressExecutor,
	IMultiConnectorListener {
	
	private IStressExecutorListener listener;
	volatile private Map<ISocket, StressInjector> injectors;
	volatile private Set<ISocket> toComplete;
	volatile private IStressStore store;
	private Queue<IStress> queue = new ConcurrentLinkedQueue<IStress>();
	private Stress stress;
	public StressExecutor() {
		this.injectors = new HashMap<>();
		this.toComplete = new HashSet<ISocket>();
		this.listener = NullStressExecutorListener.INSTANCE;
		this.store = NullStressStore.INSTANCE;
	}
	public void setStoreResults(IStressStore store) {
		this.store = store != null ? store : NullStressStore.INSTANCE;
	}
	public void setListener(IStressExecutorListener listener) {
		this.listener = listener != null ? listener : NullStressExecutorListener.INSTANCE;
	}
	public void execute(Stress stress) {
		if (this.stress == null) {
			prepareExecution(stress);
		} else {
			queue.add(stress);
		}
	}
	private void prepareExecution(Stress stress) {
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
		this.stress = stress;
		listener.onStressStart(stress);
		for (Map.Entry<ISocket, File> e : stress.getStressFiles().entrySet()) {
			toComplete.add(e.getKey());
			StressInjector injector = new StressInjector(e.getKey(), e.getValue(), stress.getCycleTime(), stress.getMessagesPerCycle());
			injector.setListener(StressExecutor.this);
			injectors.put(e.getKey(), injector);
			injector.startInjector();
		}
	}
	public void onExecutorFinishStress() {
		for (Entry<ISocket, StressInjector> e : injectors.entrySet()) {
			e.getValue().getResult().numOfRead.set(e.getKey().getNumOfRead()-e.getValue().getResult().initialReadSocket.get());
			e.getValue().getResult().numOfWrite.set(e.getKey().getNumOfWrite()-e.getValue().getResult().initialWriteSocket.get());
		}
		List<InjectorResult> results = getInjectorResults();
		listener.onStressFinish(stress, results);
		stress = null;
		toComplete.clear();
		injectors.clear();
		if (!queue.isEmpty()) {
			prepareExecution(stress);
		}
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
		injector.getResult().numOfWrite.set(injector.getResult().getInjectorMessagesInjected());
		listener.onInjectorFinish(injector.getResult());
		toComplete.remove(socket);
		InjectorResult ir = injector.getResult();
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
		ir.numOfCycle.incrementAndGet();
		long numOfWrite = socket.getNumOfWrite()-ir.initialWriteSocket.get();
		long numOfRead = socket.getNumOfRead()-ir.initialReadSocket.get();
		ir.numOfReadCycle.set(numOfRead-ir.numOfRead.get());
		ir.numOfWriteCycle.set(numOfWrite-ir.numOfWrite.get());
		ir.numOfRead.set(numOfRead);
		ir.numOfWrite.set(numOfWrite);
		ir.ratioResponseCycle.set(ir.numOfReadCycle.get()/(0D+ir.numOfWriteCycle.get()));
		ir.sumOfRatioResponse.set(ir.sumOfRatioResponse.get()+ir.ratioResponseCycle.get());
		store.storeCycleResults(injector.getResult());
		listener.onInjectorFinishCycle(injector.getResult());
	}
	public List<InjectorResult> getInjectorResults() {
		List<InjectorResult> results = new ArrayList<>();
		for (Entry<ISocket, StressInjector> e : injectors.entrySet()) {
			results.add(e.getValue().getResult());
		}
		return results;
	}
	@Override
	public void startExecutor() {
	}
	@Override
	public void stopExecutor() {
	}
	@Override
	public void setStore(IStressStore store) {
		this.store = store != null ? store : NullStressStore.INSTANCE;
	}
}
