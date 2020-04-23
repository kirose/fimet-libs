package com.fimet.core.stress.exe;

import java.util.HashMap;
import java.util.HashSet;
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

public class StressExecutor implements IMessengerListener, IInjectorListener, IExecutor, IConnectorOnConnectAll, IConnectorOnConnect {
	private IStress stress;
	volatile private IExecutorListener listener;
	volatile private Map<IMessenger, IInjector> injectors;
	volatile private Map<IMessenger, InjectorResult> injectorResults;
	volatile private Map<IMessenger, MessengerResult> messengerResults;
	private Set<IMessenger> toComplete;
	public StressExecutor(IStress stress) {
		this.injectors = new HashMap<>();
		this.messengerResults = new HashMap<>();
		this.injectorResults = new HashMap<>();
		this.toComplete = new HashSet<IMessenger>();
		this.stress = stress;
		this.listener = NullExecutorListener.INSTANCE;
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
		MessengerResult mr = new MessengerResult(messenger.getConnection().toString());
		mr.initialReadSocket.set(messenger.getSocket().getNumOfRead());
		mr.initialWriteSocket.set(messenger.getSocket().getNumOfWrite());
		messengerResults.put(messenger, mr);
		if (stress.getStressFile(iSocket) != null) {
			toComplete.add(messenger);
			StressInjector injector = new StressInjector(messenger, mr, stress.getStressFile(iSocket), stress.getCycleTime(), stress.getMessagesPerCycle());
			injector.setListener(StressExecutor.this);
			injectors.put(messenger, injector);
			injectorResults.put(messenger, injector.getResult());
		}
	}
	@Override
	public void onConnectorConnectAll(IConnectable connectable) {
		_execute();
	}
	public void _execute() {
		for (Map.Entry<IMessenger, IInjector> i : injectors.entrySet()) {
			i.getValue().startInjector();
		}
	}
	public void onExecutorFinishExecution() {
		for (Entry<IMessenger, MessengerResult> e : messengerResults.entrySet()) {
			e.getValue().numOfRead.set(e.getKey().getSocket().getNumOfRead()-e.getValue().initialReadSocket.get());
			e.getValue().numOfWrite.set(e.getKey().getSocket().getNumOfWrite()-e.getValue().initialWriteSocket.get());
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
		if (!toComplete.isEmpty() && injectors.containsKey(messenger) && injectors.get(messenger).isFinished()) {
			MessengerResult mr = messengerResults.get(messenger);
			InjectorResult ir = injectors.get(messenger).getResult();
			mr.numOfRead.set(messenger.getSocket().getNumOfRead()-mr.initialReadSocket.get());
			if (ir.getInjectorMessagesInjected() == mr.getNumOfRead() && !mr.hasFinished.get()) {
				mr.hasFinished.set(true);
				mr.startTime = ir.injectorStartTime;
				mr.finishTime = System.currentTimeMillis();		
				toComplete.remove(messenger);
				if (toComplete.isEmpty()) {
					onExecutorFinishExecution();
				}
			} 
		}
	}
	@Override
	synchronized public void onInjectorStartInject(IInjector injector) {
		listener.onInjectorStart(injector);
	}
	@Override
	synchronized public void onInjectorFinishInject(IInjector injector) {
		messengerResults.get(injector.getMessenger()).numOfWrite.set(injector.getResult().getInjectorMessagesInjected());
		listener.onInjectorFinish(injector);
	}
	@Override
	public void onInjectorStartCycle(IInjector injector) {}
	@Override
	public void onInjectorFinishCycle(IInjector injector) {
		IAdaptedSocket socket = injector.getMessenger().getSocket();
		IMessenger m = injector.getMessenger();
		MessengerResult mr = messengerResults.get(m);
		mr.numOfCycle.incrementAndGet();
		long numOfWrite = socket.getNumOfWrite()-mr.initialWriteSocket.get();
		long numOfRead = socket.getNumOfRead()-mr.initialReadSocket.get();
		mr.numOfReadCycle.set(numOfRead-mr.numOfRead.get());
		mr.numOfWriteCycle.set(numOfWrite-mr.numOfWrite.get());
		mr.numOfRead.set(numOfRead);
		mr.numOfWrite.set(numOfWrite);
		mr.ratioResponseCycle.set(mr.numOfReadCycle.get()/(0D+mr.numOfWriteCycle.get()));
		mr.sumOfRatioResponse.set(mr.sumOfRatioResponse.get()+mr.ratioResponseCycle.get());
		listener.onInjectorFinishCycle(injector);
	}
	@Override
	public Map<IMessenger, MessengerResult> getMessengerResults() {
		return messengerResults;
	}
	@Override
	public Map<IMessenger, InjectorResult> getInjectorResults() {
		return injectorResults;
	}
}
