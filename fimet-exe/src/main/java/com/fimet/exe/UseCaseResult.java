package com.fimet.exe;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import com.fimet.assertions.IAssertionResult;
import com.fimet.simulator.ISimulator;

public class UseCaseResult implements IResult {
	public enum State {
		START, STOPPED, COMPLETE, TIMEOUT, ERROR, CONNECTION_REFUSED
	}
	private State status;
	private AtomicLong startTime = new AtomicLong();
	private AtomicLong finishTime = new AtomicLong();
	private Map<ISimulator, IAssertionResult[]> simulatorValidations;
	public UseCaseResult() {
		this.simulatorValidations = new HashMap<>();
	}
	public Map<ISimulator, IAssertionResult[]> getSimulatorValidations() {
		return simulatorValidations;
	}
	public State getState() {
		return status;
	}
	public void setState(State status) {
		this.status = status;
	}
	public long getStartTime() {
		return startTime.get();
	}
	public void setStartTime(long statTime) {
		this.startTime.set(statTime);
	}
	public long getFinishTime() {
		return finishTime.get();
	}
	public void setFinishTime(long finishTime) {
		this.finishTime.set(finishTime);
	}
	public String toString() {
		return new Timestamp(getStartTime())+","+(getFinishTime()-getStartTime())+","+status;
	}
}
