package com.fimet.stress;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.fimet.FimetException;
import com.fimet.FimetLogger;
import com.fimet.exe.SocketResult;
import com.fimet.exe.Task;
import com.fimet.exe.IStore;
import com.fimet.utils.JsonUtils;

public class StressCycleStore implements IStore {
	FileWriter store;
	public StressCycleStore() {
	}
	public void storeCycleResults(SocketResult result) {
		if (store != null) { 
			try {
				EStressCycleStore e = new EStressCycleStore();
				e.setAddress(result.getAddress());
				e.setPort(result.getPort());
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
	public void init(Task task, Object...params) {
		try {
			File file = new File(task.getFolder(), "/stress-detail.json");
			store = new java.io.FileWriter(file);
		} catch (IOException e) {
			throw new FimetException(e);
		}
	}
	public void close() {
		if (store !=null) {
			try {store.close();} catch(Throwable e) {}
			store = null;
		}
	}
	public class EStressCycleStore {
		private Long numOfCycle;
		private String address;
		private Integer port;
		private Long numOfWrite;
		private Long numOfRead;
		private Long numOfApprovals;
		public Long getNumOfCycle() {
			return numOfCycle;
		}
		public void setNumOfCycle(Long numOfCycle) {
			this.numOfCycle = numOfCycle;
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
		public Long getNumOfApprovals() {
			return numOfApprovals;
		}
		public void setNumOfApprovals(Long numOfApprovals) {
			this.numOfApprovals = numOfApprovals;
		}
		public Long getNumOfWrite() {
			return numOfWrite;
		}
		public void setNumOfWrite(Long numOfWrite) {
			this.numOfWrite = numOfWrite;
		}
		public Long getNumOfRead() {
			return numOfRead;
		}
		public void setNumOfRead(Long numOfRead) {
			this.numOfRead = numOfRead;
		}
	}
	@Override
	public void save() {
	}

}
