package com.fimet.stress;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import com.fimet.FimetException;
import com.fimet.FimetLogger;
import com.fimet.exe.SocketResult;
import com.fimet.utils.JsonUtils;

public class StressCycleStore {
	File STORE_PATH = new File("store/task");
	FileWriter store;
	public StressCycleStore() {
	}
	public void storeCycleResults(SocketResult result) {
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

}
