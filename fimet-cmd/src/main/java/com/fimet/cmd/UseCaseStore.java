package com.fimet.cmd;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fimet.FimetException;
import com.fimet.FimetLogger;
import com.fimet.exe.UseCaseResult;
import com.fimet.exe.UseCaseResult.Status;
import com.fimet.simulator.ISimulator;
import com.fimet.simulator.ValidationResult;
import com.fimet.usecase.IUseCase;
import com.fimet.usecase.IUseCaseStore;
import com.fimet.utils.JsonUtils;

public class UseCaseStore extends SimulatorStore implements IUseCaseStore {
	File STORE_PATH = new File("store/task");
	FileWriter store;
	public UseCaseStore() {
	}
	@Override
	public void storeUseCase(IUseCase useCase) {
		if (store != null) { 
			try {
				EUseCaseStore e = new EUseCaseStore();
				UseCaseResult result = useCase.getResult();
				e.setName(useCase.getName());
				e.setStartTime(result.getStartTime());
				e.setExecutionTime(result.getFinishTime()-result.getStartTime());
				e.setStatus(result.getStatus());
				List<ISimulator> simulators = useCase.getSimulators();
				Simulator[] psimulators = new Simulator[simulators.size()];
				Map<ISimulator, ValidationResult[]> vals = result.getSimulatorValidations();
				int i = 0;
				for (ISimulator is : simulators) {
					Simulator s = new Simulator();
					s.setModel(is.getModel().getName());
					s.setAddress(is.getSocket().getAddress());
					s.setPort(is.getSocket().getPort());
					if (vals.containsKey(is)) {
						s.setValidations(vals.get(is));
					}
					psimulators[i++] = s;
				}
				e.setSimulators(psimulators);
				String line = JsonUtils.toJson(e)+"\n";
				synchronized (this) {
					store.write(line);
				}
				store.flush();
			} catch (Throwable ex) {
				FimetLogger.error(getClass(), ex);
			}
		}
	}
	@Override
	public void open(UUID idTask) {
		super.open(idTask);
		if (store != null) {
			throw new FimetException("Unable to open, you must call close before this operation");
		}
		try {
			store = new java.io.FileWriter(new File(STORE_PATH, ""+idTask+"/usecase.json"));
		} catch (IOException e) {
			throw new FimetException(e);
		}
	}
	@Override
	public void close(UUID idTask) {
		super.close(idTask);
		if (store !=null) {
			try {store.close();} catch(Throwable e) {}
			store = null;
		}
	}
	public class EUseCaseStore {
		private String name;
		private Status status;
		private Long startTime;
		private Long executionTime;
		private Map<String, String> data;
		private Simulator[] simulators;
		public EUseCaseStore() {
			data = new HashMap<>();
		}
		public EUseCaseStore(String name) {
			super();
			this.name = name;
			data = new HashMap<>();
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Long getStartTime() {
			return startTime;
		}
		public void setStartTime(Long startTime) {
			this.startTime = startTime;
		}
		public Long getExecutionTime() {
			return executionTime;
		}
		public void setExecutionTime(Long executionTime) {
			this.executionTime = executionTime;
		}
		public Map<String, String> getData() {
			return data;
		}
		public void setData(Map<String, String> data) {
			this.data = data;
		}
		public Simulator[] getSimulators() {
			return simulators;
		}
		public void setSimulators(Simulator[] simulators) {
			this.simulators = simulators;
		}
		public Status getStatus() {
			return status;
		}
		public void setStatus(Status status) {
			this.status = status;
		}
	}
	public class Simulator {
		private String model;
		private String address;
		private Integer port;
		private ValidationResult[] validations;
		public String getModel() {
			return model;
		}
		public void setModel(String model) {
			this.model = model;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public Integer getPort() {
			return port;
		}
		public void setPort(Integer port) {
			this.port = port;
		}
		public ValidationResult[] getValidations() {
			return validations;
		}
		public void setValidations(ValidationResult[] validations) {
			this.validations = validations;
		}
	}
}
