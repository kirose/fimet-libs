package com.fimet.exe;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class UseCaseFolderResult {
	public enum Status {
		START,COMPLETE, ERROR
	}
	String name;
	Status status;
	AtomicInteger numOfUseCases = new AtomicInteger(0);
	AtomicLong startTime = new AtomicLong(0);
	AtomicLong finishTime = new AtomicLong(0);
	AtomicInteger numOfComplete = new AtomicInteger(0);
	AtomicInteger numOfError = new AtomicInteger(0);
	AtomicInteger numOfTimeout = new AtomicInteger(0);
	public String getName() {
		return name;
	}
	void setName(String name) {
		this.name = name;
	}
	public Status getStatus() {
		return status;
	}
	public int getNumOfUseCases() {
		return numOfUseCases.get();
	}
	void setNumOfUseCases(int numOfUseCases) {
		this.numOfUseCases.set(numOfUseCases);
	}
	int getNumOfUseCasesAndIncrement() {
		return numOfUseCases.getAndIncrement();
	}
	public long getStartTime() {
		return startTime.get();
	}
	void setStartTime(long startTime) {
		this.startTime.set(startTime);
	}
	public long getFinishTime() {
		return finishTime.get();
	}
	void setFinishTime(long finishTime) {
		this.finishTime.set(finishTime);
	}
	public int getNumOfComplete() {
		return numOfComplete.get();
	}
	int getNumOfCompleteAndIncrement() {
		return numOfComplete.getAndIncrement();
	}
	void setNumOfComplete(int numOfComplete) {
		this.numOfComplete.set(numOfComplete);
	}
	public int getNumOfError() {
		return numOfError.get();
	}
	int getNumOfErrorAndIncrement() {
		return numOfError.getAndIncrement();
	}
	void setNumOfError(int numOfError) {
		this.numOfError.set(numOfError);
	}
	public int getNumOfTimeout() {
		return numOfTimeout.get();
	}
	int getNumOfTimeoutAndIncrement() {
		return numOfTimeout.getAndIncrement();
	}
	void setNumOfTimeout(int numOfTimeout) {
		this.numOfTimeout.set(numOfTimeout);
	}
}
