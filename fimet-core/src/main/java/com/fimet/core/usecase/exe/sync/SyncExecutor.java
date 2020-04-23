package com.fimet.core.usecase.exe.sync;

import com.fimet.commons.FimetLogger;
import com.fimet.core.IMessengerManager;
import com.fimet.core.ISocketManager;
import com.fimet.core.Manager;
import com.fimet.core.iso8583.parser.Message;
import com.fimet.core.net.Connector;
import com.fimet.core.net.Connector.IConnectable;
import com.fimet.core.net.Connector.IConnectorOnConnectAll;
import com.fimet.core.net.IMessenger;
import com.fimet.core.net.IMessengerListener;
import com.fimet.core.usecase.IUseCase;
import com.fimet.core.usecase.exe.ExecutionResult;
import com.fimet.core.usecase.exe.ExecutionStatus;
import com.fimet.core.usecase.exe.IExecutor;
import com.fimet.core.usecase.exe.IExecutorMonitor;
import com.fimet.core.usecase.exe.NullExecutorMonitor;

import java.util.concurrent.LinkedBlockingQueue;

public class SyncExecutor extends Thread implements IExecutor, IMessengerListener, IUseCaseTimerListener, IConnectorOnConnectAll {
	static ISocketManager socketManager = Manager.get(ISocketManager.class);
	static IMessengerManager messengerManager = Manager.get(IMessengerManager.class);

	private boolean alive;
	private LinkedBlockingQueue<IUseCase> queue;
	private UseCaseTimer useCaseTimer;
	private IUseCase useCase;
	private IExecutorMonitor monitor;
	private ExecutionResult result;
	private IMessenger messengerAcquirer;
	public SyncExecutor() {
		super("SynExecutor-Thread");
		this.queue = new LinkedBlockingQueue<IUseCase>();
		this.alive = true;
		this.useCaseTimer = new UseCaseTimer();
		this.useCaseTimer.start();
		this.monitor = NullExecutorMonitor.INSTANCE;
	}
	public void execute(IUseCase useCase) {
		new Connector(useCase, this).connectAsync();
	}
	@Override
	public void onConnectorConnectAll(IConnectable connectable) {
		this.queue.add(useCase);		
	}
	public void stopExecution() {
		queue.clear();
		onStop();
	}

	@Override
	public void startExecutor() {
		this.start();
	}
	@Override
	public void stopExecutor() {
		this.useCaseTimer.stopTimer();
		this.alive = false;
		this.queue.clear();
		this.notifyAll();
	}
	public void setMonitor(IExecutorMonitor monitor) {
		this.monitor = monitor != null ? monitor : NullExecutorMonitor.INSTANCE;
	}
	@Override
	public void onMessengerConnected(IMessenger messenger) {}

	@Override
	public void onMessengerConnecting(IMessenger messenger) {}

	@Override
	public void onMessengerDisconnected(IMessenger messenger) {}

	@Override
	synchronized public void onMessengerWriteMessage(IMessenger messenger, Message message) {
		if (useCase != null) {
			result.getOrPutSocketData(messenger.getConnection()).setOutMessage(message);
			useCase.getValidator().onWriteMessage(messenger.getConnection(), message);
		}
	}
	@Override
	synchronized public void onMessengerReadMessage(IMessenger messenger, Message message) {
		if (useCase != null) {
			result.getOrPutSocketData(messenger.getConnection()).setInMessage(message);
			useCase.getValidator().onReadMessage(messenger.getConnection(), message);
		}
		if (messenger == messengerAcquirer) {
			onComplete();
		}
	}
	@Override
	synchronized public void onMessengerReadFromSocket(IMessenger messenger, byte[] message) {
		if (result != null) {
			result.getOrPutSocketData(messenger.getConnection()).setInTime(System.currentTimeMillis());
			result.getOrPutSocketData(messenger.getConnection()).setInBytes(message);
		}
	}
	@Override
	synchronized public void onMessengerWriteToSocket(IMessenger messenger, byte[] message) {
		if (result != null) {
			result.getOrPutSocketData(messenger.getConnection()).setOutTime(System.currentTimeMillis());
			result.getOrPutSocketData(messenger.getConnection()).setOutBytes(message);
		}
	}

	@Override
	public void timeout(IUseCase useCase) {
		if (this.useCase == useCase) {
			onTimeout();
		}
	}
	private void onStart() {
		result.setStatus(ExecutionStatus.START);
		monitor.onStart(useCase);
	}
	private void onTimeout() {
		result.setStatus(ExecutionStatus.TIMEOUT);
		onFinish();
	}
	private void onStop() {
		FimetLogger.debug(SyncExecutor.class, "Stop "+useCase+" "+this.getName());
		result.setStatus(ExecutionStatus.STOPPED);
		onFinish();
	}
	private void onError() {
		FimetLogger.debug(SyncExecutor.class, "Error "+useCase+" "+this.getName());
		result.setStatus(ExecutionStatus.ERROR);
		onFinish();
	}
	private void onComplete() {
		FimetLogger.debug(SyncExecutor.class, "Complete "+useCase+" "+this.getName());
		result.setStatus(ExecutionStatus.COMPLETE);
		useCaseTimer.remove(useCase);
		onFinish();
	}
	synchronized private void onFinish() {
		FimetLogger.debug(SyncExecutor.class, "Finish "+useCase+" "+this.getName());
		monitor.onFinish(useCase, result);
		if (currentThread().getState() == Thread.State.WAITING) {
			this.notify();
		}
		IUseCase next = queue.peek();
		if (result.getStatus() == ExecutionStatus.COMPLETE &&
			next != null &&
			next.getAuthorization() != null &&
			next.getAuthorization().equals(useCase.getName())
		) {
			Message msg = result.getSocketData(useCase.getAcquirer()).getInMessage();
			next.getMessage().setValue("38", msg.getValue(38));
			next.getMessage().setValue("39", msg.getValue(39));
		}
		this.useCase = null;
		this.result = null;
		this.monitor = NullExecutorMonitor.INSTANCE;
		this.messengerAcquirer = null;
	}
	synchronized private void onExecute(IUseCase useCase) {
		try {
			this.useCase = useCase;
			this.result = new ExecutionResult();
			this.onStart();
			this.messengerAcquirer = messengerManager.getMessenger(useCase.getAcquirer()); 
			this.useCaseTimer.add(useCase, this);
			this.messengerAcquirer.writeMessage(useCase.getMessage());
			this.wait();
		} catch (Exception e) {
			FimetLogger.error("Thread error", e);
			useCaseTimer.remove(useCase);
			onError();
		}
	}
	public void run() {
		try {
			while (alive) {
				IUseCase next = queue.take();
				if (next != null)
					onExecute(next);
			}
		} catch (Exception e) {
			FimetLogger.error("Thread error", e);
			run();
		}
	}
	@Override
	public void removeMonitor() {
		monitor = NullExecutorMonitor.INSTANCE;
	}
}
