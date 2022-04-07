package com.fimet.exe;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

public class StressMultiResult implements IResult {
	private File folder;
	private String name;
	private AtomicInteger numOfStress = new AtomicInteger(0);
	private StressTask task;
	
	public StressMultiResult(StressTask task) {
		this.task = task;
	}
	public File getFolder() {
		return folder;
	}
	public void setFolder(File folder) {
		this.folder = folder;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNumOfStress() {
		return numOfStress.get();
	}
	public void setNumOfStress(int numOfStress) {
		this.numOfStress.set(numOfStress);
	}
	public int getNumOfStressAndIncrement() {
		return numOfStress.getAndIncrement();
	}
	public StressTask getTask() {
		return task;
	}
	public void setTask(StressTask task) {
		this.task = task;
	}
	@Override
	public String toString() {
		return task.toString();
	}
}
