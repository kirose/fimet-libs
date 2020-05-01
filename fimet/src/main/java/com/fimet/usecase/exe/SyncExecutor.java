package com.fimet.usecase.exe;

import com.fimet.ISessionManager;
import com.fimet.ISimulatorManager;
import com.fimet.ISocketManager;
import com.fimet.Manager;
import com.fimet.commons.FimetLogger;
import com.fimet.commons.exception.FimetException;
import com.fimet.iso8583.parser.Message;
import com.fimet.net.IConnectable;
import com.fimet.net.MultiConnector;
import com.fimet.net.MultiConnector.IMultiConnectorListener;
import com.fimet.net.MultiConnector.IMultiConnectable;
import com.fimet.simulator.ISimulator;
import com.fimet.simulator.ISimulatorListener;
import com.fimet.simulator.ValidationResult;
import com.fimet.usecase.IUseCase;
import com.fimet.usecase.Session;
import com.fimet.usecase.UseCase;
import com.fimet.utils.UseCaseUtils;

import java.io.File;
import java.util.concurrent.ConcurrentLinkedQueue;;

public class SyncExecutor
implements
	IExecutor,
	ISimulatorListener,
	ISessionListener,
	IMultiConnectorListener {

	static ISocketManager socketManager = Manager.get(ISocketManager.class);
	static ISimulatorManager simulatorManager = Manager.get(ISimulatorManager.class);
	static ISessionManager contextManager = Manager.get(ISessionManager.class);
	static ISessionManager sessionManager = Manager.get(ISessionManager.class);
	
	private boolean alive;
	private ConcurrentLinkedQueue<Object> queue;
	private IExecutorListener listener;
	private IStoreResults store;
	private ExecutorThread executor;
	private QueueIterator iterator;
	public SyncExecutor() {
		this.queue = new ConcurrentLinkedQueue<Object>();
		this.alive = true;
		this.listener = NullExecutorListener.INSTANCE;
		this.iterator = new QueueIterator();
	}
	public void execute(File fileOrFolder) {
		this.queue.add(fileOrFolder);
		checkThread();
	}
	public void execute(UseCase useCase) {
		this.queue.add(useCase);
		checkThread();
	}
	private void checkThread() {
		if (executor == null) {
			executor = new ExecutorThread();
			executor.start();
		}
	}
	public void setStore(IStoreResults store) {
		this.store = store != null ? store : NullStoreResults.INSTANCE;
	}
	@Override
	public void startExecutor() {
	}
	@Override
	public void stopExecutor() {
		this.alive = false;
		this.queue.clear();
		wakeUp();
	}
	public void setListener(IExecutorListener listener) {
		this.listener = listener != null ? listener : NullExecutorListener.INSTANCE;
	}

	@Override
	public byte[] onSimulatorWriteMessage(ISimulator simulator, Message message) {
		Session session = sessionManager.getSession(message);
		if (session != null && session.getUseCase() != null) {
			UseCase useCase = session.getUseCase();
			try {
				message = useCase.getSimulatorExtension().simulateOutgoingMessage(simulator, message);
			} catch (Throwable e) {
				FimetLogger.warning(SyncExecutor.class, "SimulatorExtension error on write, "+simulator+" "+useCase.getSimulatorExtension().getClass().getName(), e);
			}
			if (message == null) {
				return null;
			}
			byte[] iso = simulator.getParser().formatMessage(message);
			try {
				store.storeOutgoing(simulator, useCase, message, iso);
			} catch (Throwable e) {
				FimetLogger.warning(SyncExecutor.class, "Store error on read message", e);
			}
			return iso;
		} else {
			byte[] iso = simulator.getParser().formatMessage(message);
			try {
				store.storeOutgoing(simulator, null, message, iso);
			} catch (Throwable e) {
				FimetLogger.warning(SyncExecutor.class, "Store error on read message", e);
			}
			return iso;
		}
	}
	@Override
	public void onSimulatorReadMessage(ISimulator simulator, Message message, byte[] bytes) {
		Session session = sessionManager.getSession(message);
		if (session != null && session.getUseCase() != null) {
			UseCase useCase = session.getUseCase();
			ValidationResult[] vals = null;
			try {
				vals = useCase.getSimulatorExtension().validateIncomingMessage(simulator, message);
			} catch (Throwable e) {
				FimetLogger.warning(SyncExecutor.class, "Validation error on read message, "+simulator, e);
			}
			try {
				store.storeIncoming(simulator, useCase, vals, message, bytes);
			} catch (Throwable e) {
				FimetLogger.warning(SyncExecutor.class, "Store error on read message", e);
			}
			useCase.getResult().setFinishTime(System.currentTimeMillis());
			if (simulator == useCase.getAcquirer()) {
				useCase.setResponse(message);
				onComplete(useCase);
			}
		} else {
			try {
				store.storeIncoming(simulator, null, null, message, bytes);
			} catch (Throwable e) {
				FimetLogger.warning(SyncExecutor.class, "Store error on read message", e);
			}
		}
	}
	public void stop(UseCase useCase) {
		onStop(useCase);
	}
	@Override
	public void timeout(UseCase useCase) {
		onTimeout(useCase);
	}
	private void onStart(UseCase useCase) {
		useCase.getResult().setStatus(ExecutionStatus.START);
		useCase.getResult().setStatTime(System.currentTimeMillis());
		store.storeStart(useCase);
		listener.onStart(useCase);
	}
	private void onTimeout(UseCase useCase) {
		useCase.getResult().setStatus(ExecutionStatus.TIMEOUT);
		onFinish(useCase);
	}
	private void onStop(UseCase useCase) {
		FimetLogger.debug(SyncExecutor.class, "Stop "+useCase+" "+this);
		useCase.getResult().setStatus(ExecutionStatus.STOPPED);
		onFinish(useCase);
	}
	private void onError(UseCase useCase) {
		FimetLogger.debug(SyncExecutor.class, "Error "+useCase+" "+this);
		useCase.getResult().setStatus(ExecutionStatus.ERROR);
		onFinish(useCase);
	}
	private void onComplete(UseCase useCase) {
		FimetLogger.debug(SyncExecutor.class, "Complete "+useCase+" "+this);
		useCase.getResult().setStatus(ExecutionStatus.COMPLETE);
		onFinish(useCase);
	}
	private void onFinish(UseCase useCase) {
		contextManager.deleteSession(useCase);
		useCase.getResult().setFinishTime(System.currentTimeMillis());
		FimetLogger.debug(SyncExecutor.class, "Finish "+useCase+" "+this);
		store.storeFinish(useCase, useCase.getResult());
		listener.onFinish(useCase, useCase.getResult());
		IUseCase next = iterator.peek();
		if (useCase.getResult().getStatus() == ExecutionStatus.COMPLETE &&
			next != null &&
			next.getAuthorization() != null &&
			next.getAuthorization().equals(useCase.getName()) && 
			useCase.getResponse() != null
		) {
			next.getMessage().setValue("38", useCase.getResponse().getValue(38));
			next.getMessage().setValue("39", useCase.getResponse().getValue(39));
		}
		wakeUp();
	}
	private void wakeUp() {
		if (executor != null && executor.getState() == Thread.State.WAITING) {
			synchronized (executor) {
				executor.notify();
			}
		}
	}
	private boolean areSimulatorsConnected(UseCase useCase) {
		for (ISimulator s : useCase.getSimulators()) {
			if (!s.isConnected()) {
				return false;
			}
		}
		return true;
	}
	@Override
	public void onConnectorConnect(IConnectable simulator) {}
	@Override
	public void onConnectorTimeout(IMultiConnectable connectable) {
		if (connectable instanceof UseCase) {
			UseCase useCase = (UseCase)connectable;
			FimetLogger.error(SyncExecutor.class, "Cannot connect simulators for use case "+useCase.getName());
			onStop(useCase);
		}
	}
	@Override
	public void onConnectorConnectAll(IMultiConnectable connectable) {
		doExecute((UseCase)connectable);
	}
	private void doExecute(UseCase useCase) {
		try {
			for (ISimulator s : useCase.getSimulators()) s.setListener(this);
			contextManager.createSession(useCase, this);
			useCase.getAcquirer().writeMessage(useCase.getMessage());
		} catch (Exception e) {
			FimetLogger.error("Execution error", e);
			onError(useCase);
		}
	}
	private void doPrepareExecution(UseCase useCase) {
		try {
			useCase.setResult(new ExecutionResult());
			this.onStart(useCase);
			if (areSimulatorsConnected(useCase)) {
				doExecute(useCase);
			} else {
				new MultiConnector(useCase, this).connectAsync();	
			}
		} catch (Exception e) {
			FimetLogger.error("Execution error", e);
			onError(useCase);
		}
	}

	class ExecutorThread extends Thread {
		public ExecutorThread() {
			super("SyncExecutorThread");
		}
		public void run() {
			try {
				UseCase next;
				while (alive && iterator.hasNext()) {
					next = iterator.next();
					if (next != null) {
						doPrepareExecution(next);
					}
					synchronized (this) {
						this.wait();
					}
					if (!iterator.hasNext()) {
						alive = false;
					}
				}
			} catch (Exception e) {
				FimetLogger.error("Thread error", e);
			}
			executor = null;
		}
	}
	public String toString() {
		return SyncExecutor.class.getSimpleName();
	}
	private class QueueIterator {
		FolderIterator folderIterator = null;
		UseCase next;
		public boolean hasNext() {
			if (next != null) {
				return true;
			} else if (folderIterator != null) {
				if (folderIterator.hasNext()) {
					next = folderIterator.next();
					return true;
				} else {
					folderIterator = null;
					return hasNext();
				}
			} else if (queue.isEmpty()) {
				return false;
			} else {
				Object o = queue.poll();
				if (o instanceof UseCase) {
					next = (UseCase)o;
					return true;
				} else if (o instanceof File) {
					File file = (File)o;
					if (!file.exists()) {
						throw new FimetException("File not exists "+file.getAbsolutePath());
					}
					if (file.isFile()) {
						try {
							next = UseCaseUtils.parseForExecution(file);
							return true;
						} catch (Exception e) {
							FimetLogger.error(SyncExecutor.class, "Cannot execute file "+file, e);
							return false;
						}
					} else if (file.isDirectory()) {
						folderIterator = new FolderIterator(file);
						if (folderIterator.hasNext()) {
							next = folderIterator.next();
							return true;
						} else {
							folderIterator = null;
							return hasNext();
						}
					} else {
						throw new FimetException("Invalid executable object "+o);
					}
				} else {
					throw new FimetException("Invalid executable object "+o);
				}
			}
		}
		public UseCase peek() {
			if (hasNext()) {
				return next;
			}
			return null;
		}
		public UseCase next() {
			UseCase tmp = next;
			next = null;
			return tmp;
		}
	}
}
