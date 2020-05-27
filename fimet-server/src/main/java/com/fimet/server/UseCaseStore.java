package com.fimet.server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fimet.FimetException;
import com.fimet.FimetLogger;
import com.fimet.entity.EUseCaseStore;
import com.fimet.exe.UseCaseResult;
import com.fimet.pojo.Simulator;
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

}
