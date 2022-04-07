package com.fimet.stress;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;import org.slf4j.Logger;

import com.fimet.FimetException;

import com.fimet.exe.SocketResult;
import com.fimet.exe.Task;
import com.fimet.exe.IStore;
import com.fimet.utils.JsonUtils;

public class StressCycleStore implements IStore {
	private static Logger logger = LoggerFactory.getLogger(StressCycleStore.class);
	private Map<String, FileWriter> writers = new HashMap<String, FileWriter>();
	private File folder;
	public StressCycleStore() {
	}
	public void storeCycleResults(SocketResult result) {
		FileWriter writer = getWriter(result.getName());
		if (writer != null) { 
			try {
				EStressCycleStore e = new EStressCycleStore();
				e.setName(result.getName());
				e.setAddress(result.getAddress());
				e.setPort(result.getPort());
				e.setNumOfCycle(result.getNumOfCycle());
				e.setNumOfWrite(result.getNumOfWriteCycle());
				e.setNumOfRead(result.getNumOfReadCycle());
				e.setNumOfApprovals(result.getNumOfApprovalsCycle());
				String line = JsonUtils.toJson(e)+"\n";
				synchronized (this) {
					writer.write(line);
					writer.flush();
				}
			} catch (Throwable ex) {
				logger.error("Store stress exception", ex);
			}
		}
	}
	public void init(Task task, Object...params) {
		folder = task.getFolder();
	}
	public void close() {
		if (writers !=null&&!writers.isEmpty()) {
			for(Map.Entry<String, FileWriter> e : writers.entrySet()) {
				try {e.getValue().close();} catch(Throwable ex) {}
			}
		}
	}
	private FileWriter getWriter(String name) {
		if (!writers.containsKey(name)) {
			try {
				File file = new File(folder, name+"-socket.json");
				java.io.FileWriter writer = new java.io.FileWriter(file);
				writers.put(name, writer);
				return writer;
			} catch (IOException e) {
				throw new FimetException(e);
			}
		}
		return writers.get(name);
	}
	public class EStressCycleStore {
		private Long numOfCycle;
		private String name;
		private String address;
		private Integer port;
		private Long numOfWrite;
		private Long numOfRead;
		private Long numOfApprovals;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
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
