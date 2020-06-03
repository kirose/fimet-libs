package com.fimet.exe;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import com.fimet.simulator.ISimulator;
	import com.fimet.simulator.ValidationResult;

public class UseCaseResult {
	public enum Status {
		START, STOPPED, COMPLETE, TIMEOUT, ERROR, CONNECTION_REFUSED
	}
	Status status;
	AtomicLong startTime = new AtomicLong();
	AtomicLong finishTime = new AtomicLong();
	Map<ISimulator, ValidationResult[]> simulatorValidations;
	UseCaseResult() {
		this.simulatorValidations = new HashMap<>();
	}
	public Map<ISimulator, ValidationResult[]> getSimulatorValidations() {
		return simulatorValidations;
	}
	public Status getStatus() {
		return status;
	}
	void setStatus(Status status) {
		this.status = status;
	}
	public long getStartTime() {
		return startTime.get();
	}
	void setStartTime(long statTime) {
		this.startTime.set(statTime);
	}
	public long getFinishTime() {
		return finishTime.get();
	}
	void setFinishTime(long finishTime) {
		this.finishTime.set(finishTime);
	}
	public String toString() {
		return new Timestamp(getStartTime())+","+(getFinishTime()-getStartTime())+","+status;
	}
}
