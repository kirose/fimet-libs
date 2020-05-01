package com.fimet.usecase.exe;

import java.util.concurrent.atomic.AtomicLong;

public class ExecutionResult {
	ExecutionStatus status;
	AtomicLong statTime = new AtomicLong();
	AtomicLong finishTime = new AtomicLong();
	public ExecutionResult() {
		super();
	}
	public ExecutionStatus getStatus() {
		return status;
	}
	public void setStatus(ExecutionStatus status) {
		this.status = status;
	}
	public long getStatTime() {
		return statTime.get();
	}
	public void setStatTime(long statTime) {
		this.statTime.set(statTime);
	}
	public long getFinishTime() {
		return finishTime.get();
	}
	public void setFinishTime(long finishTime) {
		this.finishTime.set(finishTime);
	}
}
