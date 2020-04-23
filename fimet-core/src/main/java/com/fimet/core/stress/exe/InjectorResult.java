package com.fimet.core.stress.exe;

import java.util.concurrent.atomic.AtomicLong;

public class InjectorResult {
	String name;
	AtomicLong injectorMessagesInjected = new AtomicLong(0L);
	AtomicLong injectorMessagesInjectedCycle = new AtomicLong(0L);
	AtomicLong injectorStartCycleTime = new AtomicLong(0L);
	AtomicLong injectorFinishCycleTime = new AtomicLong(0L);
	long injectorStartTime;
	long injectorFinishTime;
	long readerNumberOfWaits;
	long readerMessagesRead;
	long readerStartTime;
	long readerFinishTime;
	public InjectorResult(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public long getInjectorMessagesInjected() {
		return injectorMessagesInjected.get();
	}
	public long getInjectorStartTime() {
		return injectorStartTime;
	}
	public long getInjectorFinishTime() {
		return injectorFinishTime;
	}
	public long getReaderNumberOfWaits() {
		return readerNumberOfWaits;
	}
	public long getReaderMessagesRead() {
		return readerMessagesRead;
	}
	public long getReaderStartTime() {
		return readerStartTime;
	}
	public long getReaderFinishTime() {
		return readerFinishTime;
	}
	public long getInjectorDurationTime() {
		if (injectorFinishTime > injectorStartTime) {
			return injectorFinishTime-injectorStartTime;
		} else {
			return System.currentTimeMillis()-injectorStartTime;
		}
	}
	public long getReaderDurationTime() {
		if (readerFinishTime>readerStartTime) {
			return readerFinishTime-readerStartTime;
		} else {
			return System.currentTimeMillis()-readerStartTime;
		}
	}
	public long getInjectorStartCycleTime() {
		return injectorStartCycleTime.get();
	}
	public long getInjectorFinishCycleTime() {
		return injectorFinishCycleTime.get();
	}
	public long getInjectorDurationCycleTime() {
		return injectorFinishCycleTime.get()-injectorStartCycleTime.get();
	}
	public long getInjectorMessagesInjectedCycle() {
		return injectorMessagesInjectedCycle.get();
	}
	public String toString() {
		return 	getStatsGlobal();
	}
	public String getStatsGlobal() {
		return "InjectWrite:"+getInjectorMessagesInjected()+",InjectTime:"+getInjectorDurationTime();
	}
	public String getStatsCycle() {
		return "IW:"+getInjectorMessagesInjectedCycle()+",IT:"+getInjectorDurationCycleTime();
	}
}
