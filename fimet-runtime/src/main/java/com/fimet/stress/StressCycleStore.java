package com.fimet.stress;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import com.fimet.FimetException;
import com.fimet.FimetLogger;
import com.fimet.exe.InjectorResult;
import com.fimet.utils.JsonUtils;
import com.fimet.entity.EStressCycleStore;

public class StressCycleStore {
	File STORE_PATH = new File("store/task");
	FileWriter store;
	public StressCycleStore() {
	}
	public void storeCycleResults(InjectorResult result) {
		if (store != null) { 
			try {
				EStressCycleStore e = new EStressCycleStore();
				e.setAddress(result.getSocket().getAddress());
				e.setPort(result.getSocket().getPort());
				e.setNumOfCycle(result.getNumOfCycle());
				e.setNumOfWrite(result.getNumOfWriteCycle());
				e.setNumOfRead(result.getNumOfReadCycle());
				e.setNumOfApprovals(result.getNumOfApprovalsCycle());
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
		if (store != null) {
			throw new FimetException("Unable to open, you must call close before this operation");
		}
		try {
			new File(STORE_PATH, ""+idTask).mkdirs();
			File file = new File(STORE_PATH, ""+idTask+"/stress-detail.json");
			store = new java.io.FileWriter(file);
		} catch (IOException e) {
			throw new FimetException(e);
		}
	}
	public void close(UUID idTask) {
		if (store !=null) {
			try {store.close();} catch(Throwable e) {}
			store = null;
		}
	}
}
