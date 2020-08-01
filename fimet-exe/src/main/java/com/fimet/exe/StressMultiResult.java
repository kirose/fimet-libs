package com.fimet.exe;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class StressMultiResult implements IResult {
	public enum Status {
		START,COMPLETE, ERROR
	}
	private File folder;
	private String name;
	private Status status;
	private AtomicLong startTime = new AtomicLong(0);
	private AtomicLong finishTime = new AtomicLong(0);
	private AtomicInteger numOfStress = new AtomicInteger(0);
	
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
	public Status getStatus() {
		return status;
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
	public long getStartTime() {
		return startTime.get();
	}
	public void setStartTime(long startTime) {
		this.startTime.set(startTime);
	}
	public long getFinishTime() {
		return finishTime.get();
	}
	public void setFinishTime(long finishTime) {
		this.finishTime.set(finishTime);
	}
	public void setStatus(Status status) {
		this.status = status;
	}
}
