package com.fimet.stress;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fimet.exe.StressResult;
import com.fimet.net.IConnectable;
import com.fimet.simulator.ISimulator;

public class Stress implements IStress {
	private String name;
	private int cycleTime;
	private int messagesPerCycle;
	private long maxExecutionTime = 1000L*30;
	private Map<ISimulator, File> stressFiles;
	private StressResult result;
	private Object source;
	public Stress() {
		name = "Unkow Stress";
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map<ISimulator, File> getStressFiles() {
		return stressFiles;
	}
	public void setStressFiles(Map<ISimulator, File> stressFiles) {
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
	public StressResult getResult() {
		return result;
	}
	public void setResult(StressResult result) {
		this.result = result;
	}
	public Object getSource() {
		return source;
	}
	public void setSource(Object source) {
		this.source = source;
	}
	@Override
	public List<IConnectable> getConnectables() {
		List<IConnectable> connectables = new ArrayList<>();
		for (Map.Entry<ISimulator,File> e : stressFiles.entrySet()) {
			connectables.add(e.getKey());
		}
		return connectables;
	}
}
