package com.fimet.stress;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fimet.net.IConnectable;
import com.fimet.net.ISocket;

public class Stress implements IStress {
	private String name;
	private int cycleTime;
	private int messagesPerCycle;
	private long maxExecutionTime = 1000L*30;
	private Map<ISocket, File> stressFiles;
	public Stress() {
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map<ISocket, File> getStressFiles() {
		return stressFiles;
	}
	public void setStressFiles(Map<ISocket, File> stressFiles) {
		this.stressFiles = stressFiles;
	}
	public void setMaxExecutionTime(long maxExecutionTime) {
		this.maxExecutionTime = maxExecutionTime;
	}
	@Override
	public long getMaxExecutionTime() {
		return maxExecutionTime;
	}
	public int getCycleTime() {
		return cycleTime;
	}
	public void setCycleTime(int cycleTime) {
		this.cycleTime = cycleTime;
	}
	public int getMessagesPerCycle() {
		return messagesPerCycle;
	}
	public void setMessagesPerCycle(int messagesPerCycle) {
		this.messagesPerCycle = messagesPerCycle;
	}
	@Override
	public List<IConnectable> getConnectables() {
		List<IConnectable> connectables = new ArrayList<>();
		for (Map.Entry<ISocket,File> e : stressFiles.entrySet()) {
			connectables.add(e.getKey());
		}
		return connectables;
	}
}
