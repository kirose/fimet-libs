package com.fimet.json;

import java.util.List;

public class JStress {
	private String name;
	private int cycleTime;
	private int messagesPerCycle;
	private long maxExecutionTime = 1000L*30;
	private List<JStressFile> stressFiles;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public long getMaxExecutionTime() {
		return maxExecutionTime;
	}
	public void setMaxExecutionTime(long maxExecutionTime) {
		this.maxExecutionTime = maxExecutionTime;
	}
	public List<JStressFile> getStressFiles() {
		return stressFiles;
	}
	public void setStressFiles(List<JStressFile> stressFiles) {
		this.stressFiles = stressFiles;
	}
	
}
