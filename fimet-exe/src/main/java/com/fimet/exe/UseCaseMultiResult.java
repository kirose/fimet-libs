package com.fimet.exe;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class UseCaseMultiResult implements IResult {
	public enum Status {
		START,COMPLETE, ERROR
	}
	private File folder;
	private String name;
	private Status status;
	private AtomicLong startTime = new AtomicLong(0);
	private AtomicLong finishTime = new AtomicLong(0);
	private AtomicInteger numOfUseCases = new AtomicInteger(0);
	private AtomicInteger numOfComplete = new AtomicInteger(0);
	private AtomicInteger numOfError = new AtomicInteger(0);
	private AtomicInteger numOfTimeout = new AtomicInteger(0);
	
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
	public int getNumOfUseCases() {
		return numOfUseCases.get();
	}
	public void setNumOfUseCases(int numOfUseCases) {
		this.numOfUseCases.set(numOfUseCases);
	}
	public int getNumOfUseCasesAndIncrement() {
		return numOfUseCases.getAndIncrement();
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
	public int getNumOfComplete() {
		return numOfComplete.get();
	}
	public int getNumOfCompleteAndIncrement() {
		return numOfComplete.getAndIncrement();
	}
	public void setNumOfComplete(int numOfComplete) {
		this.numOfComplete.set(numOfComplete);
	}
	public int getNumOfError() {
		return numOfError.get();
	}
	public int getNumOfErrorAndIncrement() {
		return numOfError.getAndIncrement();
	}
	public void setNumOfError(int numOfError) {
		this.numOfError.set(numOfError);
	}
	public int getNumOfTimeout() {
		return numOfTimeout.get();
	}
	public int getNumOfTimeoutAndIncrement() {
		return numOfTimeout.getAndIncrement();
	}
	public void setNumOfTimeout(int numOfTimeout) {
		this.numOfTimeout.set(numOfTimeout);
	}
	public void setStatus(Status status) {
		this.status = status;
	}
}
