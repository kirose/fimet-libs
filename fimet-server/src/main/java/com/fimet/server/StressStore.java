package com.fimet.server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import com.fimet.FimetException;
import com.fimet.FimetLogger;
import com.fimet.entity.EStressStore;
import com.fimet.exe.InjectorResult;
import com.fimet.stress.IStressStore;
import com.fimet.stress.StressCycleStore;
import com.fimet.utils.JsonUtils;

public class StressStore extends StressCycleStore implements IStressStore {
	File STORE_PATH = new File("store/task");
	FileWriter store;
	public StressStore() {
	}
	@Override
	public void storeGlobalResults(InjectorResult result) {
		if (store != null) { 
			try {
				EStressStore e = new EStressStore();
				e.setAddress(result.getSocket().getAddress());
				e.setPort(result.getSocket().getPort());
				e.setNumOfApprovals(result.getNumOfApprovals());
				e.setNumOfRead(result.getNumOfRead());
				e.setNumOfWrite(result.getNumOfWrite());
				e.setNumOfInjec(result.getInjectorMessagesInjected());
				e.setCycleTime(result.getCycleTime());
				e.setCycles(result.getNumOfCycle());
				e.setInjectStartTime(result.getInjectorStartTime());
				e.setInjectFinishTime(result.getInjectorFinishTime());
				e.setName(result.getName());
				String line = JsonUtils.toJson(e)+"\n";
				synchronized (this) {
					store.write(line);
					store.flush();
				}
			} catch (Throwable ex) {
				FimetLogger.error(getClass(), ex);
			}
		}
	}
	public void open(UUID idTask) {
		super.open(idTask);
		if (store != null) {
			throw new FimetException("Unable to open, you must call close before this operation");
		}
		try {
			store = new java.io.FileWriter(new File(STORE_PATH, ""+idTask+"/stress.json"));
		} catch (IOException e) {
			throw new FimetException(e);
		}
	}
	public void close(UUID idTask) {
		super.close(idTask);
		if (store !=null) {
			try {store.close();} catch(Throwable e) {}
			store = null;
		}
	}
}
