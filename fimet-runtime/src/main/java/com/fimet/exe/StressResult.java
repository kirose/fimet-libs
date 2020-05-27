package com.fimet.exe;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class StressResult {
	String name;
	AtomicInteger numOfInjectors = new AtomicInteger(0);
	AtomicLong startTime = new AtomicLong(0);
	AtomicLong finishTime = new AtomicLong(0);
	List<InjectorResult> injectorResults;
	public StressResult() {
	}
	public String getName() {
		return name;
	}
	void setName(String name) {
		this.name = name;
	}
	public int getNumOfInjectors() {
		return numOfInjectors.get();
	}
	void setNumOfInjectors(int numOfUseCases) {
		this.numOfInjectors.set(numOfUseCases);
	}
	int getNumOfInjectorsAndIncrement() {
		return numOfInjectors.getAndIncrement();
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
	public List<InjectorResult> getInjectorResults() {
		return injectorResults;
	}
	void setInjectorResults(List<InjectorResult> injectorResults) {
		this.injectorResults = injectorResults;
	}
}
