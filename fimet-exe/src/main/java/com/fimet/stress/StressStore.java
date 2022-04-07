package com.fimet.stress;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.LoggerFactory;import org.slf4j.Logger;

import com.fimet.FimetException;

import com.fimet.exe.SocketResult;
import com.fimet.exe.Task;
import com.fimet.stress.IStressStore;
import com.fimet.utils.JsonUtils;

public class StressStore extends StressCycleStore implements IStressStore {
	private static Logger logger = LoggerFactory.getLogger(StressStore.class);
	private FileWriter writer;
	public StressStore() {
	}
	@Override
	public void storeGlobalResults(SocketResult result) {
		if (writer != null) { 
			try {
				EStressStore e = new EStressStore();
				e.setName(result.getName());
				e.setAddress(result.getAddress());
				e.setPort(result.getPort());
				e.setNumOfApprovals(result.getNumOfApprovals());
				e.setNumOfRead(result.getNumOfRead());
				e.setNumOfWrite(result.getNumOfWrite());
				e.setNumOfInjec(result.getInjectorMessagesInjected());
				e.setMsgsInFile(result.getReaderMessagesRead());
				e.setMessagesPerCycle(result.getMessagesPerCycle());
				e.setCycleTime(result.getCycleTime());
				e.setCycles(result.getNumOfCycle());
				e.setInjectStartTime(result.getInjectorStartTime());
				e.setInjectFinishTime(result.getInjectorFinishTime());
				String line = JsonUtils.toJson(e)+"\n";
				synchronized (this) {
					writer.write(line);
					writer.flush();
				}
			} catch (Throwable ex) {
				logger.error("Stores stress exception", ex);
			}
		}
	}
	public void init(Task task, Object...params) {
		if (!task.getFolder().exists()) {
			task.getFolder().mkdirs();
		}
		super.init(task, params);
		try {
			writer = new java.io.FileWriter(new File(task.getFolder(), "/stress.json"));
		} catch (IOException e) {
			throw new FimetException(e);
		}
	}
	public void close() {
		super.close();
		if (writer !=null) {
			try {writer.close();} catch(Throwable e) {}
			writer = null;
		}
	}
	public class EStressStore {
		private String name;
		private String address;
		private Integer port;
		private Long time;
		private Long cycleTime;
		private Long cycles;
		private Long messagesPerCycle;
		private Long numOfWrite;
		private Long numOfRead;
		private Long numOfApprovals;
		private Long numOfInjec;
		private Long msgsInFile;
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
		public Long getMessagesPerCycle() {
			return messagesPerCycle;
		}
		public void setMessagesPerCycle(Long messagesPerCycle) {
			this.messagesPerCycle = messagesPerCycle;
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
		public void setMsgsInFile(Long msgInFile) {
			this.msgsInFile = msgInFile;
		}
		public Long getMsgsInFile() {
			return msgsInFile;
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
