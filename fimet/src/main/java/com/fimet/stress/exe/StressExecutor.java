package com.fimet.stress.exe;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fimet.net.ISocket;
import com.fimet.stress.IStress;

import java.util.Set;

public class StressExecutor
implements 
	IInjectorListener,
	IExecutor {
	
	private IStress stress;
	private IExecutorListener listener;
	volatile private Map<ISocket, StressInjector> injectors;
	volatile private Set<ISocket> toComplete;
	volatile private IStoreResults store; 
	public StressExecutor(IStress stress) {
		this.injectors = new HashMap<>();
		this.toComplete = new HashSet<ISocket>();
		this.stress = stress;
		this.listener = NullExecutorListener.INSTANCE;
		this.store = NullStoreResults.INSTANCE;
	}
	public void setStoreResults(IStoreResults store) {
		this.store = store != null ? store : NullStoreResults.INSTANCE;
	}
	public void setListener(IExecutorListener listener) {
		this.listener = listener != null ? listener : NullExecutorListener.INSTANCE;
	}
	public void execute() {
		listener.onExecutorStart(this);
		for (Map.Entry<ISocket, File> e : stress.getStressFiles().entrySet()) {
			toComplete.add(e.getKey());
			StressInjector injector = new StressInjector(e.getKey(), e.getValue(), stress.getCycleTime(), stress.getMessagesPerCycle());
			injector.setListener(StressExecutor.this);
			injectors.put(e.getKey(), injector);
			injector.startInjector();
		}
	}
	public void onExecutorFinishExecution() {
		for (Entry<ISocket, StressInjector> e : injectors.entrySet()) {
			e.getValue().getResult().numOfRead.set(e.getKey().getNumOfRead()-e.getValue().getResult().initialReadSocket.get());
			e.getValue().getResult().numOfWrite.set(e.getKey().getNumOfWrite()-e.getValue().getResult().initialWriteSocket.get());
		}
		listener.onExecutorFinish(this);
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
			onExecutorFinishExecution();
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
	@Override
	public List<InjectorResult> getInjectorResults() {
		List<InjectorResult> results = new ArrayList<>();
		for (Entry<ISocket, StressInjector> e : injectors.entrySet()) {
			results.add(e.getValue().getResult());
		}
		return results;
	}
}
