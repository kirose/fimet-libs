package com.fimet.usecase;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;import org.slf4j.Logger;

import com.fimet.FimetException;

import com.fimet.assertions.IAssertionResult;
import com.fimet.assertions.Status;
import com.fimet.exe.Task;
import com.fimet.exe.UseCaseResult;
import com.fimet.exe.UseCaseTask;
import com.fimet.simulator.ISimulator;
import com.fimet.usecase.IUseCase;
import com.fimet.usecase.IUseCaseStore;
import com.fimet.utils.JsonUtils;

public class UseCaseStore extends SimulatorStore implements IUseCaseStore {
	private static Logger logger = LoggerFactory.getLogger(UseCaseStore.class);
	private UseCaseTask task;
	private FileWriter writer;
	public UseCaseStore() {
	}
	@Override
	public void init(Task task, Object... params) {
		this.task = (UseCaseTask)task;
		if (!task.getFolder().exists()) {
			task.getFolder().mkdirs();
		}
		super.init(task, params);
		try {
			writer = new java.io.FileWriter(new File(task.getFolder(), "usecase.json"));
		} catch (IOException e) {
			throw new FimetException(e);
		}
	}
	@Override
	public void storeProperty(IUseCase useCase, String name, String value) {
	}
	@Override
	public void save() {
	}
	@Override
	public void storeUseCase(IUseCase useCase) {
		if (writer != null) { 
			try {
				RUseCase e = new RUseCase();
				UseCaseResult result = useCase.getResult();
				e.setName(useCase.getName());
				e.setAcquirer(useCase.getAcquirer().getName());
				if (useCase.getResponse()!=null) {
					e.setAuthorizationCode(useCase.getResponse().getValue(38));
					e.setResponseCode(useCase.getResponse().getValue(39));
				}
				e.setStartTime(result.getStartTime());
				e.setExecutionTime(result.getFinishTime()-result.getStartTime());
				e.setStatus(result.getState()!=null?result.getState().toString():null);
				List<ISimulator> simulators = useCase.getSimulators();
				List<IRSimulator> psimulators = new ArrayList<>(simulators.size());
				Map<ISimulator, IAssertionResult[]> vals = result.getSimulatorValidations();
				Status status = Status.SUCCESS;
				for (ISimulator is : simulators) {
					RSimulator s = new RSimulator();
					s.setName(is.getName());
					s.setModel(is.getModel().getName());
					s.setParser(is.getParser().getName());
					s.setAddress(is.getSocket().getAddress());
					s.setPort(is.getSocket().getPort());
					if (vals.containsKey(is) && vals.get(is)!=null) {
						List<IRAssertion> assertions = new ArrayList<>(vals.size());
						for (IAssertionResult ar : vals.get(is)) {
							if (ar.getStatus() != Status.SUCCESS) {
								status = Status.FAIL;
							}
							assertions.add(new RAssertion(ar));
						}
						s.setAssertions(assertions);
					}
					psimulators.add(s);
				}
				e.setStatusAssertions(status.toString());
				e.setSimulators(psimulators);
				String line = JsonUtils.toJson(e)+"\n";
				synchronized (this) {
					writer.write(line);
				}
				writer.flush();
				task.onEntityUpdated(e);
			} catch (Throwable ex) {
				logger.error("Store usecase exception", ex);
			}
		}
	}
	@Override
	public void close() {
		super.close();
		if (writer !=null) {
			try {writer.close();} catch(Throwable e) {}
			writer = null;
		}
	}
}
