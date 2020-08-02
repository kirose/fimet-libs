package com.fimet.exe;

import com.fimet.FimetLogger;
import com.fimet.ISessionManager;
import com.fimet.ISimulatorManager;
import com.fimet.ISocketManager;
import com.fimet.IStoreManager;
import com.fimet.Manager;
import com.fimet.assertions.IAssertionResult;
import com.fimet.exe.UseCaseResult.Status;
import com.fimet.exe.usecase.NullUseCaseMonitor;
import com.fimet.exe.usecase.NullUseCaseStore;
import com.fimet.net.IConnectable;
import com.fimet.net.IMultiConnectable;
import com.fimet.parser.IMessage;
import com.fimet.simulator.ISimulator;
import com.fimet.simulator.ISimulatorListener;
import com.fimet.socket.MultiConnector;
import com.fimet.socket.IMultiConnectorListener;
import com.fimet.usecase.CodeStoreDefault;
import com.fimet.usecase.ICode;
import com.fimet.usecase.ICodeStore;
import com.fimet.usecase.ISessionListener;
import com.fimet.usecase.IUseCase;
import com.fimet.usecase.IUseCaseMonitor;
import com.fimet.usecase.IUseCaseStore;
import com.fimet.usecase.Session;
import com.fimet.usecase.UseCase;
import com.fimet.utils.UseCaseUtils;

import java.io.File;

public class UseCaseExecutor implements
	IUseCaseExecutor,
	ISimulatorListener,
	ISessionListener,
	IMultiConnectorListener {
	
	static ISocketManager socketManager = Manager.get(ISocketManager.class);
	static ISimulatorManager simulatorManager = Manager.get(ISimulatorManager.class);
	static ISessionManager sessionManager = Manager.get(ISessionManager.class);
	static IStoreManager storeManager = Manager.get(IStoreManager.class);
	
	private IUseCaseListener listener;
	private IUseCaseMonitor monitor;
	private ICodeStore codeStore;
	private IUseCaseStore store;
	public UseCaseExecutor() {
		this.store = NullUseCaseStore.INSTANCE;
		this.monitor = NullUseCaseMonitor.INSTANCE;
		this.codeStore = Manager.get(ICodeStore.class, CodeStoreDefault.class);
	}
	public void cancel() {
	}
	public void execute(UseCase useCase) {
		doPrepareExecution(useCase);
	}
	public void execute(File useCaseFile) {
		UseCase useCase = (UseCase)UseCaseUtils.fromFile(useCaseFile);
		doPrepareExecution(useCase);
	}
	private void doPrepareExecution(UseCase useCase) {
		try {
			UseCaseResult result = new UseCaseResult();
			useCase.setResult(result);
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
			for (ISimulator s : useCase.getSimulators()) s.setListener(this);
			useCase.getMessage().setProperty(IMessage.USECASE, useCase);
			useCase.getAcquirer().writeMessage(useCase.getMessage());
		} catch (Exception e) {
			FimetLogger.error(UseCaseExecutor.class,"Execution error", e);
			onError(useCase);
		}
	}
	@Override
	public byte[] onSimulatorWriteMessage(ISimulator simulator, IMessage message) {
		IUseCase useCase = null;
		if (message.hasProperty(IMessage.USECASE)) { 
			useCase = (IUseCase)message.getProperty(IMessage.USECASE);
		} else {
			Session session = sessionManager.getSession(message);
			if (session != null)
				useCase = session.getUseCase();
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
			IAssertionResult[] vals = null;
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
		monitor.onUseCaseStart(useCase);
		listener.onUseCaseStart(useCase);
	}
	private void onTimeout(IUseCase useCase) {
		useCase.getResult().setStatus(Status.TIMEOUT);
		onFinish(useCase);
	}
	//public void onStop(IUseCase useCase) {
	//	FimetLogger.debug(UseCaseExecutor.class, "Stop "+useCase);
	//	useCase.getResult().setStatus(Status.STOPPED);
	//	onFinish(useCase);
	//}
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
		monitor.onUseCaseFinish(useCase, useCase.getResult());
		listener.onUseCaseFinish(useCase);
	}
	private boolean areSimulatorsConnected(UseCase useCase) {
		for (ISimulator s : useCase.getSimulators()) {
			if (!s.isConnected()) {
				return false;
			}
		}
		return true;
	}
	public String toString() {
		return UseCaseExecutor.class.getSimpleName();
	}
	@Override
	public void start() {
		
	}
	@Override
	public void reload() {
	}
	public void setStore(IUseCaseStore store) {
		this.store = store != null ? store : NullUseCaseStore.INSTANCE;
		simulatorManager.setStore(store);
		socketManager.setStore(store);
	}
	public void setMonitor(IUseCaseMonitor monitor) {
		this.monitor = monitor!=null?monitor:NullUseCaseMonitor.INSTANCE;
	}
	@Override
	public void setListener(IUseCaseListener listener) {
		this.listener = listener!=null?listener:NullUseCaseListener.INSTANCE;
	}
}
