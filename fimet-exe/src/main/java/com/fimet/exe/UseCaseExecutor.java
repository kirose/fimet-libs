package com.fimet.exe;


import com.fimet.ISessionManager;
import com.fimet.ISimulatorManager;
import com.fimet.ISocketManager;
import com.fimet.Manager;
import com.fimet.assertions.IAssertionResult;
import com.fimet.exe.UseCaseResult.State;
import com.fimet.exe.usecase.NullUseCaseMonitor;
import com.fimet.exe.usecase.NullUseCaseStore;
import com.fimet.net.IConnectable;
import com.fimet.net.IMultiConnectable;
import com.fimet.net.IMultiConnectorListener;
import com.fimet.net.MultiConnector;
import com.fimet.parser.IMessage;
import com.fimet.simulator.ISimulator;
import com.fimet.simulator.ISimulatorListener;
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

import javax.annotation.PostConstruct;

import org.slf4j.LoggerFactory;import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UseCaseExecutor implements
	IUseCaseExecutor,
	ISimulatorListener,
	ISessionListener,
	IMultiConnectorListener {
	private static Logger logger = LoggerFactory.getLogger(UseCaseExecutor.class);
	@Autowired private ISocketManager socketManager;
	@Autowired private ISimulatorManager simulatorManager;
	@Autowired private ISessionManager sessionManager;
	@Autowired private Manager manager;
	
	private IUseCaseListener listener;
	private IUseCaseMonitor monitor;
	private ICodeStore codeStore;
	private IUseCaseStore store;
	private String[] authorizationFields;
	public UseCaseExecutor() {
		String fields = Manager.getProperty("usecase.store.fields", "38,39");
		authorizationFields = fields.split(",");
	}
	@PostConstruct
	public void start() {
		this.store = NullUseCaseStore.INSTANCE;
		this.monitor = NullUseCaseMonitor.INSTANCE;
		this.codeStore = manager.get(ICodeStore.class, CodeStoreDefault.class);
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
			logger.error("Execution error", e);
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
			logger.error("Execution error", e);
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
				logger.warn("SimulatorExtension error on write, "+simulator+" "+useCase.getSimulatorExtension().getClass().getName(), e);
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
				logger.warn("Validation error on read message, "+simulator, e);
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
			logger.debug("Connection Refused "+useCase);
			useCase.getResult().setState(State.CONNECTION_REFUSED);
			onFinish(useCase);				
		}
	}
	@Override
	public void onSessionExpire(IUseCase useCase) {
		onTimeout(useCase);
	}
	private void onStart(UseCase useCase) {
		logger.debug("Start {}"+useCase);
		useCase.getResult().setState(State.START);
		useCase.getResult().setStartTime(System.currentTimeMillis());
		if (useCase.getAuthorization() != null) {
			ICode code = codeStore.get(useCase.getAuthorization());
			if (code != null) {
				for (int i = 0; i < authorizationFields.length; i++) {
					useCase.getMessage().setValue(authorizationFields[i], code.getValue(i));
				}
			}
		}
		monitor.onUseCaseStart(useCase);
		listener.onUseCaseStart(useCase);
	}
	private void onTimeout(IUseCase useCase) {
		useCase.getResult().setState(State.TIMEOUT);
		onFinish(useCase);
	}
	//public void onStop(IUseCase useCase) {
	//	logger.debug(UseCaseExecutor.class, "Stop "+useCase);
	//	useCase.getResult().setStatus(Status.STOPPED);
	//	onFinish(useCase);
	//}
	private void onError(UseCase useCase) {
		useCase.getResult().setState(State.ERROR);
		onFinish(useCase);
	}
	private void onComplete(IUseCase useCase) {
		useCase.getResult().setState(State.COMPLETE);
		onFinish(useCase);
	}
	private void onFinish(IUseCase useCase) {
		sessionManager.deleteSession(useCase);
		useCase.getResult().setFinishTime(System.currentTimeMillis());
		logger.debug("Finish "+useCase+" with status "+useCase.getResult().getState());
		try {
			store.storeUseCase(useCase);
		} catch (Throwable e) {
			logger.error("UseCaseStore exception",e);	
		}
		if (useCase.getResult().getState() == State.COMPLETE && useCase.getResponse()!=null) {
			try {
				codeStore.store(useCase);
			} catch (Throwable e) {
				logger.error("CodeStore exception",e);	
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
	@Override
	public void stop() {}
}
