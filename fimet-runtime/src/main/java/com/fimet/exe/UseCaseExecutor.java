package com.fimet.exe;

import com.fimet.FimetLogger;
import com.fimet.ISessionManager;
import com.fimet.ISimulatorManager;
import com.fimet.ISocketManager;
import com.fimet.Manager;
import com.fimet.exe.UseCaseResult.Status;
import com.fimet.exe.usecase.FolderIterator;
import com.fimet.exe.usecase.IFinishListener;
import com.fimet.exe.usecase.NullFinishListener;
import com.fimet.exe.usecase.NullUseCaseExecutorListener;
import com.fimet.exe.usecase.NullUseCaseStore;
import com.fimet.parser.IMessage;
import com.fimet.simulator.ISimulator;
import com.fimet.simulator.ISimulatorListener;
import com.fimet.simulator.ValidationResult;
import com.fimet.socket.IConnectable;
import com.fimet.socket.MultiConnector;
import com.fimet.socket.IMultiConnectable;
import com.fimet.socket.MultiConnector.IMultiConnectorListener;
import com.fimet.usecase.ISessionListener;
import com.fimet.usecase.IUseCase;
import com.fimet.usecase.CodeStoreDefault;
import com.fimet.usecase.ICode;
import com.fimet.usecase.ICodeStore;
import com.fimet.usecase.IUseCaseExecutor;
import com.fimet.usecase.IUseCaseExecutorListener;
import com.fimet.usecase.IUseCaseStore;
import com.fimet.usecase.Session;
import com.fimet.usecase.UseCase;
import com.fimet.utils.UseCaseUtils;

import java.io.File;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class UseCaseExecutor implements IUseCaseExecutor, IFinishListener {
	static ISocketManager socketManager = Manager.get(ISocketManager.class);
	static ISimulatorManager simulatorManager = Manager.get(ISimulatorManager.class);
	static ISessionManager sessionManager = Manager.get(ISessionManager.class);

	private FolderExecutor folderExecutor;
	private SingleExecutor singleExecutor;
	private ICodeStore codeStore;
	private IUseCaseStore store;
	private IExecutorListener listener;
	private Queue<Task> queue = new ConcurrentLinkedQueue<Task>();
	private Task executing;
	public UseCaseExecutor() {
		codeStore = Manager.getExtension(ICodeStore.class, CodeStoreDefault.class);
		folderExecutor = new FolderExecutor();
		folderExecutor.setFinishListener(this);
		singleExecutor = new SingleExecutor();
		singleExecutor.setFinishListener(this);
	}
	@Override
	public void execute(Task task) {
		validate(task);
		queue.add(task);
		checkNext();
	}
	private void checkNext() {
		if (executing == null && !queue.isEmpty()) {
			executing = queue.poll();
			executing.state = Task.State.START;
			simulatorManager.setStore(store);
			socketManager.setStore(store);
			store.open(executing.getId());
			if (executing.getSource() instanceof UseCase) {
				singleExecutor.execute((UseCase)executing.getSource());
			} else if (executing.getSource() instanceof File) {
				File file = (File)executing.getSource(); 
				if (file.isFile()) {
					singleExecutor.execute(file);
				} else {
					folderExecutor.execute(file);
				}
			} else {
				this.executing = null;
				throw new ExecutionException("Invalid execution type "+executing.getSource());
			}
		}
	}
	private void validate(Task task) {
		if (!(task.getSource() instanceof UseCase || task.getSource() instanceof File)) {
			throw new ExecutionException("Invalid execution type "+task.getSource());
		}	
	}
	public void setStore(IUseCaseStore store) {
		this.store = store != null ? store : NullUseCaseStore.INSTANCE;
	}
	public void setListener(IUseCaseExecutorListener listener) {
		folderExecutor.setListener(listener);
		singleExecutor.setListener(listener);
	}
	@Override
	public void onFinish(Object result) {
		if (executing != null) {
			executing.result = result;
			simulatorManager.setStore(null);
			socketManager.setStore(null);
			store.close(executing.getId());
			listener.onTaskFinish(executing);
			executing = null;
		}
		checkNext();
	}
	@Override
	public void cancel() {
		executing = null;
		folderExecutor.cancel();
		singleExecutor.cancel();
	}
	@Override
	public void setExecutorListener(IExecutorListener listener) {
		this.listener = listener!=null?listener:NullExecutorListener.INSTANCE;
	}
	private class FolderExecutor extends SingleExecutor implements IFinishListener {
		private FolderIterator iterator;
		private IFinishListener finishListener;
		private UseCaseFolderResult result;
		public FolderExecutor() {
			this.finishListener = NullFinishListener.INSTANCE;
			super.setFinishListener(this);
		}
		public void cancel() {
			if (iterator != null) {
				iterator.close();
				iterator = null;
			}
		}
		@Override
		public void execute(UseCase useCase) {
			
			super.execute(useCase);
		}
		public void execute(File folder) {
			try {
				this.result = new UseCaseFolderResult();
				result.setName(folder.getName());
				result.status = UseCaseFolderResult.Status.START;
				this.result.setStartTime(System.currentTimeMillis());
				this.iterator = new FolderIterator(folder);
				System.out.println("");
				if(iterator.hasNext()) {
					execute(iterator.next());
				} else {
					result.status = UseCaseFolderResult.Status.COMPLETE;
					finishListener.onFinish(result);
				}
			} catch(Exception e) {
				FimetLogger.error(UseCaseExecutor.class, e);
				result.status = UseCaseFolderResult.Status.ERROR;
				finishListener.onFinish(result);
			}
		}
		@Override
		public void onFinish(Object result) {
			if (iterator != null) {
				this.result.getNumOfUseCasesAndIncrement();
				Status status = ((UseCaseResult)result).getStatus();
				if (status == Status.COMPLETE) {
					this.result.getNumOfCompleteAndIncrement();
				} else if (status == Status.ERROR || status == Status.CONNECTION_REFUSED) {
					this.result.getNumOfErrorAndIncrement();
				} else if (status == Status.TIMEOUT) {
					this.result.getNumOfTimeoutAndIncrement();
				}
				if (iterator.hasNext()) {
					execute(iterator.next());
				} else {
					this.result.setFinishTime(System.currentTimeMillis());
					iterator = null;
					finishListener.onFinish(this.result);
					this.result = null;
					this.iterator = null;
				}
			}
		}
		public void setFinishListener(IFinishListener finishListener) {
			this.finishListener = finishListener!=null?finishListener:NullFinishListener.INSTANCE;
		}
	}
	
	private class SingleExecutor implements
		ISimulatorListener,
		ISessionListener,
		IMultiConnectorListener {
	
		private IUseCaseExecutorListener listener;
		private IFinishListener finishListener;
		public SingleExecutor() {
			listener = NullUseCaseExecutorListener.INSTANCE;
		}
		public void cancel() {
		}
		public void execute(UseCase useCase) {
			doPrepareExecution(useCase);
		}
		public void execute(File useCaseFile) {
			UseCase useCase = UseCaseUtils.fromFile(useCaseFile);
			doPrepareExecution(useCase);
		}
		private void doPrepareExecution(UseCase useCase) {
			try {
				useCase.setResult(new UseCaseResult());
				this.onStart(useCase);
				if (areSimulatorsConnected(useCase)) {
					doExecute(useCase);
				} else {
					new MultiConnector(useCase).connectAsync(this);	
				}
			} catch (Exception e) {
				FimetLogger.error("Execution error", e);
				onError(useCase);
			}
		}
		@Override
		public void onConnectorConnectAll(IMultiConnectable connectable) {
			doExecute((UseCase)connectable);
		}
		private void doExecute(UseCase useCase) {
			try {
				FimetLogger.debug(UseCaseExecutor.class,"Start "+useCase.getName());
				for (ISimulator s : useCase.getSimulators()) s.setListener(this);
				useCase.getMessage().setProperty(IMessage.USECASE, useCase);
				useCase.getAcquirer().writeMessage(useCase.getMessage());
			} catch (Exception e) {
				FimetLogger.error(UseCaseExecutor.class,"Execution error", e);
				onError(useCase);
			}
		}
		public void setListener(IUseCaseExecutorListener listener) {
			this.listener = listener != null ? listener : NullUseCaseExecutorListener.INSTANCE;
		}
		@Override
		public byte[] onSimulatorWriteMessage(ISimulator simulator, IMessage message) {
			IUseCase useCase = null;
			if (message.hasProperty(IMessage.USECASE)) { 
				useCase = (IUseCase)message.removeProperty(IMessage.USECASE);
			} else if (sessionManager.hasSession(message)) {
				useCase = sessionManager.getSession(message).getUseCase();
			}
			if (useCase != null) {
				try {
					message = useCase.getSimulatorExtension().simulateOutgoingMessage(simulator, message);
				} catch (Throwable e) {
					FimetLogger.warning(UseCaseExecutor.class, "SimulatorExtension error on write, "+simulator+" "+useCase.getSimulatorExtension().getClass().getName(), e);
				}
				if (message == null) {
					return null;
				}
				// Session must be created after simulators execution
				// Simulators can change fields and Session-hash can be different
				if (!sessionManager.hasSession(message)) {
					sessionManager.createSession(useCase, this);
				}
				byte[] iso = simulator.getParser().formatMessage(message);
				return iso;
			} else {
				byte[] iso = simulator.getParser().formatMessage(message);
				return iso;
			}
		}
		@Override
		public void onSimulatorReadMessage(ISimulator simulator, IMessage message, byte[] bytes) {
			Session session = sessionManager.getSession(message);
			if (session != null && session.getUseCase() != null) {
				IUseCase useCase = session.getUseCase();
				ValidationResult[] vals = null;
				try {
					vals = useCase.getSimulatorExtension().validateIncomingMessage(simulator, message);
				} catch (Throwable e) {
					FimetLogger.warning(UseCaseExecutor.class, "Validation error on read message, "+simulator, e);
				}
				useCase.getResult().getSimulatorValidations().put(simulator, vals);
				useCase.getResult().setFinishTime(System.currentTimeMillis());
				if (simulator == useCase.getAcquirer()) {
					((UseCase)useCase).setResponse(message);
					onComplete(useCase);
				}
			}
		}
		@Override
		public void onConnectorConnect(IConnectable simulator) {}
		@Override
		public void onConnectorTimeout(IMultiConnectable connectable) {
			if (connectable instanceof UseCase) {
				UseCase useCase = (UseCase)connectable;
				FimetLogger.debug(UseCaseExecutor.class, "Connection Refused "+useCase);
				useCase.getResult().setStatus(Status.CONNECTION_REFUSED);
				onFinish(useCase);				
			}
		}
		@Override
		public void onSessionExpire(IUseCase useCase) {
			onTimeout(useCase);
		}
		private void onStart(UseCase useCase) {
			FimetLogger.debug(UseCaseExecutor.class, "Start "+useCase);
			useCase.getResult().setStatus(Status.START);
			useCase.getResult().setStartTime(System.currentTimeMillis());
			if (useCase.getAuthorization() != null) {
				ICode code = codeStore.get(useCase.getAuthorization());
				if (code != null) {
					useCase.getMessage().setValue("38", code.getAuthCode());
					useCase.getMessage().setValue("39", code.getResponseCode());	
				}
			}
			listener.onStart(useCase);
		}
		private void onTimeout(IUseCase useCase) {
			useCase.getResult().setStatus(Status.TIMEOUT);
			onFinish(useCase);
		}
//		public void onStop(IUseCase useCase) {
//			FimetLogger.debug(UseCaseExecutor.class, "Stop "+useCase);
//			useCase.getResult().setStatus(Status.STOPPED);
//			onFinish(useCase);
//		}
		private void onError(UseCase useCase) {
			useCase.getResult().setStatus(Status.ERROR);
			onFinish(useCase);
		}
		private void onComplete(IUseCase useCase) {
			useCase.getResult().setStatus(Status.COMPLETE);
			onFinish(useCase);
		}
		private void onFinish(IUseCase useCase) {
			sessionManager.deleteSession(useCase);
			useCase.getResult().setFinishTime(System.currentTimeMillis());
			FimetLogger.debug(UseCaseExecutor.class, "Finish "+useCase+" with status "+useCase.getResult().getStatus());
			try {
				store.storeUseCase(useCase);
			} catch (Throwable e) {
				FimetLogger.error(UseCaseExecutor.class, "UseCaseStore exception",e);	
			}
			if (useCase.getResult().getStatus() == Status.COMPLETE
				&& useCase.getResponse() != null
				&& useCase.getResponse().hasField("38")
				&& useCase.getResponse().hasField("39")) {
				try {
					codeStore.store(useCase.getName(), useCase.getResponse().getValue("38"), useCase.getResponse().getValue("39"));
				} catch (Throwable e) {
					FimetLogger.error(UseCaseExecutor.class, "CodeStore exception",e);	
				}
			}
			listener.onFinish(useCase, useCase.getResult());
			finishListener.onFinish(useCase.getResult());
		}
		private boolean areSimulatorsConnected(UseCase useCase) {
			for (ISimulator s : useCase.getSimulators()) {
				if (!s.isConnected()) {
					return false;
				}
			}
			return true;
		}
		public void setFinishListener(IFinishListener finishListener) {
			this.finishListener = finishListener!=null?finishListener:NullFinishListener.INSTANCE;
		}
		public String toString() {
			return UseCaseExecutor.class.getSimpleName();
		}
	}
}
