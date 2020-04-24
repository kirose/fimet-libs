package com.fimet.core.stress.exe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.fimet.core.iso8583.parser.Message;
import com.fimet.core.net.Connector;
import com.fimet.core.net.Connector.IConnectable;
import com.fimet.core.net.Connector.IConnectorOnConnect;
import com.fimet.core.net.Connector.IConnectorOnConnectAll;
import com.fimet.core.net.IAdaptedSocket;
import com.fimet.core.net.IMessenger;
import com.fimet.core.net.IMessengerListener;
import com.fimet.core.net.ISocket;
import com.fimet.core.stress.IStress;

public class StressExecutor
implements IMessengerListener,
	IInjectorListener,
	IExecutor,
	IConnectorOnConnectAll,
	IConnectorOnConnect {
	
	private IStress stress;
	private IExecutorListener listener;
	volatile private Map<IMessenger, StressInjector> injectors;
	volatile private Set<IMessenger> toComplete;
	volatile private IStoreResults store; 
	public StressExecutor(IStress stress) {
		this.injectors = new HashMap<>();
		this.toComplete = new HashSet<IMessenger>();
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
		new Connector(stress, this).connectAsync();
	}
	@Override
	public void onConnectorConnect(IMessenger messenger) {
		messenger.setListener(this);
		ISocket iSocket = messenger.getConnection();
		if (stress.getStressFile(iSocket) != null) {
			toComplete.add(messenger);
			StressInjector injector = new StressInjector(messenger, stress.getStressFile(iSocket), stress.getCycleTime(), stress.getMessagesPerCycle());
			injector.setListener(StressExecutor.this);
			injectors.put(messenger, injector);
		}
	}
	@Override
	public void onConnectorConnectAll(IConnectable connectable) {
		_execute();
	}
	public void _execute() {
		listener.onExecutorStart(this);
		for (Map.Entry<IMessenger, StressInjector> i : injectors.entrySet()) {
			i.getValue().startInjector();
		}
	}
	public void onExecutorFinishExecution() {
		for (Entry<IMessenger, StressInjector> e : injectors.entrySet()) {
			e.getValue().getResult().numOfRead.set(e.getKey().getSocket().getNumOfRead()-e.getValue().getResult().initialReadSocket.get());
			e.getValue().getResult().numOfWrite.set(e.getKey().getSocket().getNumOfWrite()-e.getValue().getResult().initialWriteSocket.get());
		}
		listener.onExecutorFinish(this);
	}
	@Override
	public void onMessengerConnected(IMessenger messenger) {}
	@Override
	public void onMessengerConnecting(IMessenger messenger) {}
	@Override
	public void onMessengerDisconnected(IMessenger messenger) {}
	@Override
	public void onMessengerWriteToSocket(IMessenger messenger, byte[] message) {}
	@Override
	public void onMessengerWriteMessage(IMessenger messenger, Message message) {}
	@Override
	public void onMessengerReadFromSocket(IMessenger messenger, byte[] message) {}
	@Override
	public void onMessengerReadMessage(IMessenger messenger, Message message) {
		//validatorFinish.validateExecutorFinish(messenger);
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
		IMessenger messenger = injector.getMessenger();
		injector.getResult().numOfWrite.set(injector.getResult().getInjectorMessagesInjected());
		listener.onInjectorFinish(injector.getResult());
		toComplete.remove(messenger);
		InjectorResult ir = injector.getResult();
		ir.numOfRead.set(messenger.getSocket().getNumOfRead()-ir.initialReadSocket.get());
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
		IAdaptedSocket socket = injector.getMessenger().getSocket();
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
		for (Entry<IMessenger, StressInjector> e : injectors.entrySet()) {
			results.add(e.getValue().getResult());
		}
		return results;
	}
}
