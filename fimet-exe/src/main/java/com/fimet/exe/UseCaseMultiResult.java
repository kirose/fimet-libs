package com.fimet.exe;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

public class UseCaseMultiResult implements IResult {
	private Task task;
	private File folder;
	private String name;
	private AtomicInteger numOfUseCases = new AtomicInteger(0);
	private AtomicInteger numOfComplete = new AtomicInteger(0);
	private AtomicInteger numOfError = new AtomicInteger(0);
	private AtomicInteger numOfTimeout = new AtomicInteger(0);
	
	public UseCaseMultiResult(UseCaseTask task) {
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
	public int getNumOfUseCases() {
		return numOfUseCases.get();
	}
	public void setNumOfUseCases(int numOfUseCases) {
		this.numOfUseCases.set(numOfUseCases);
	}
	public int getNumOfUseCasesAndIncrement() {
		return numOfUseCases.getAndIncrement();
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
	@Override
	public String toString() {
		return "UseCaseMultiResult [task=" + task + "]";
	}
}
