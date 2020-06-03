package com.fimet.cmd;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import com.fimet.FimetException;
import com.fimet.FimetLogger;
import com.fimet.exe.SocketResult;
import com.fimet.stress.IStressStore;
import com.fimet.stress.StressCycleStore;
import com.fimet.utils.JsonUtils;

public class StressStore extends StressCycleStore implements IStressStore {
	File STORE_PATH = new File("store/task");
	FileWriter store;
	public StressStore() {
	}
	@Override
	public void storeGlobalResults(SocketResult result) {
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
	public class EStressStore {
		private String name;
		private String address;
		private Integer port;
		private Long time;
		private Long cycleTime;
		private Long cycles;
		private Long numOfApprovals;
		private Long numOfWrite;
		private Long numOfRead;
		private Long numOfInjec;
		private Long injectStartTime;
		private Long injectFinishTime;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
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
		public Long getTime() {
			return time;
		}
		public void setTime(Long time) {
			this.time = time;
		}
		public Long getCycleTime() {
			return cycleTime;
		}
		public void setCycleTime(Long cycleTime) {
			this.cycleTime = cycleTime;
		}
		public Long getCycles() {
			return cycles;
		}
		public void setCycles(Long cycles) {
			this.cycles = cycles;
		}
		public void setNumOfApprovals(Long numOfApprovals) {
			this.numOfApprovals = numOfApprovals;
		}
		public Long getNumOfApprovals() {
			return numOfApprovals;
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
		public Long getNumOfInjec() {
			return numOfInjec;
		}
		public void setNumOfInjec(Long numOfInjec) {
			this.numOfInjec = numOfInjec;
		}
		public Long getInjectStartTime() {
			return injectStartTime;
		}
		public void setInjectStartTime(Long injectStartTime) {
			this.injectStartTime = injectStartTime;
		}
		public Long getInjectFinishTime() {
			return injectFinishTime;
		}
		public void setInjectFinishTime(Long injectFinishTime) {
			this.injectFinishTime = injectFinishTime;
		}
	}

}
