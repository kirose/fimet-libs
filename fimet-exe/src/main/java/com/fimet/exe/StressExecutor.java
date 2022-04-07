package com.fimet.exe;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import com.fimet.FimetException;
import com.fimet.ISimulatorManager;
import com.fimet.ISocketManager;
import com.fimet.exe.stress.IInjector;
import com.fimet.exe.stress.IInjectorListener;
import com.fimet.exe.stress.NullStressMonitor;
import com.fimet.exe.stress.NullStressStore;
import com.fimet.net.IConnectable;
import com.fimet.net.IMultiConnectable;
import com.fimet.net.IMultiConnectorListener;
import com.fimet.net.ISocket;
import com.fimet.net.MultiConnector;
import com.fimet.simulator.ISimulator;
import com.fimet.stress.IStressMonitor;
import com.fimet.stress.IStressStore;
import com.fimet.stress.Stress;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StressExecutor
implements 
	IInjectorListener,
	IStressExecutor,
	IMultiConnectorListener {

	@Autowired private ISocketManager socketManager;
	@Autowired private ISimulatorManager simulatorManager;
	
	private IStressMonitor monitor;
	private IStressListener listener;
	volatile private Map<ISimulator, StressInjector> injectors;
	volatile private Set<ISimulator> toComplete;
	volatile private IStressStore store;
	private Stress executing;
	private StressResult result;
	public StressExecutor() {
		this.injectors = new HashMap<ISimulator, StressInjector>();
		this.toComplete = new HashSet<ISimulator>();
		this.monitor = NullStressMonitor.INSTANCE;
		this.store = NullStressStore.INSTANCE;
		this.listener = NullStressListener.INSTANCE;
		setStore(NullStressStore.INSTANCE);
	}
	public void setStore(IStressStore store) {
		this.store = store != null ? store : NullStressStore.INSTANCE;
	}
	public void setMonitor(IStressMonitor monitor) {
		this.monitor = monitor != null ? monitor : NullStressMonitor.INSTANCE;
	}
	@Override
	public void setListener(IStressListener listener) {
		this.listener = listener!=null?listener:NullStressListener.INSTANCE;
	}
	@Override
	public void execute(Stress stress) {
		executing = stress;
		setStore(store);
		prepareExecution(stress);
	}
	@Override
	public void cancel() {
		if (executing != null) {
			for (Entry<ISimulator, StressInjector> e : injectors.entrySet()) {
				e.getValue().stopInjector();
			}
			injectors.clear();
			toComplete.clear();
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
		listener.onStressError(executing, new FimetException("Cannot connect connectables for stress "+connectable));
	}
	private void doExecute(Stress stress) {
		socketManager.setStore(null);
		simulatorManager.setStore(null);
		monitor.onStressStart(stress);
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
			ISimulator simulator = e.getKey();
			ISocket socket = simulator.getSocket();
			SocketResult result = e.getValue().getResult();
			result.numOfRead.set(socket.getNumOfRead()-e.getValue().getResult().initialReadSocket.get());
			result.numOfWrite.set(socket.getNumOfWrite()-e.getValue().getResult().initialWriteSocket.get());
			result.numOfApprovals.set(simulator.getNumOfApprovals()-e.getValue().getResult().initialApprovalsSimulator.get());
		}
		List<SocketResult> results = getInjectorResults();
		this.result.setFinishTime(System.currentTimeMillis());
		this.result.setInjectorResults(results);
		this.result.setNumOfInjectors(results.size());
		executing.setResult(this.result);

		monitor.onStressFinish(executing, results);
		listener.onStressFinish(executing);

		store.close();
		setStore(NullStressStore.INSTANCE);
		executing = null;
		result = null;
		toComplete.clear();
		injectors.clear();
	}
	public boolean hasFinish() {
		return toComplete.isEmpty();
	}
	@Override
	synchronized public void onInjectorStartInject(IInjector injector) {
		SocketResult ir = injector.getResult();
		ISimulator simulator = injector.getSimulator();
		ISocket socket = simulator.getSocket();
		ir.name = simulator.getName();
		ir.initialReadSocket.set(socket.getNumOfRead());
		ir.initialWriteSocket.set(socket.getNumOfWrite());
		ir.initialApprovalsSimulator.set(simulator.getNumOfApprovals());
		monitor.onInjectorStart(injector.getResult());
	}
	@Override
	synchronized public void onInjectorFinishInject(IInjector injector) {
		ISocket socket = injector.getSocket();
		ISimulator simulator = injector.getSimulator();
		injector.getResult().numOfWrite.set(injector.getResult().getInjectorMessagesInjected());
		monitor.onInjectorFinish(injector.getResult());
		toComplete.remove(simulator);
		SocketResult ir = injector.getResult();
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
		SocketResult ir = injector.getResult();
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
		monitor.onInjectorFinishCycle(injector.getResult());
	}
	public List<SocketResult> getInjectorResults() {
		List<SocketResult> results = new ArrayList<>();
		for (Entry<ISimulator, StressInjector> e : injectors.entrySet()) {
			results.add(e.getValue().getResult());
		}
		return results;
	}
	@PostConstruct
	@Override
	public void start() {
	}
	@Override
	public void reload() {
	}
	@Override
	public void stop() {
		
	}
}
