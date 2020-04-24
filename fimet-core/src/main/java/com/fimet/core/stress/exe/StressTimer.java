package com.fimet.core.stress.exe;

import java.sql.Timestamp;

import com.fimet.commons.FimetLogger;

public class StressTimer extends Thread implements ITimer {
	private StressInjector injector;
	private StressFileReader reader;
	private int cycleTime;
	private int messagesPerCycle;
	private boolean alive;
	public StressTimer(StressInjector injector, StressFileReader reader, int cycleTime, int messagesPerCycle) {
		this.injector = injector;
		this.reader = reader;
		this.cycleTime = cycleTime >= 0 ? cycleTime : 1000;
		this.messagesPerCycle = messagesPerCycle > 0 ? messagesPerCycle : 10;
	}
	@Override
	public void startTimer() {
		alive = true;
		this.start();
	}
	@Override
	public void stopTimer() {
		alive = false;
		wakeUp();
	}
	private void wakeUp() {
		synchronized (this) {
			if (this.getState() == Thread.State.WAITING) {
				this.notify();
			}
		}
	}
	public void run() {
		FimetLogger.debug(StressTimer.class, "Start "+this+" at "+new Timestamp(System.currentTimeMillis()));
		try {
			while(alive) {
				if (hasFinish()) {
					alive = false;
				} else {
					injector.listener.onInjectorStartCycle(injector);
					injector.result.injectorStartCycleTime.set(System.currentTimeMillis());
					injector.inject(messagesPerCycle);
					reader.read(messagesPerCycle);
					Thread.sleep(cycleTime);
				}
			}
		} catch (Exception e) {
			FimetLogger.debug(StressTimer.class, "Timer Error ",e);
		}
		FimetLogger.debug(StressTimer.class, "Finish "+this+" at "+new Timestamp(System.currentTimeMillis()));
	}
	@Override
	public boolean hasFinish() {
		return reader.hasFinish() && injector.hasFinish();
	}
	@Override
	public IReader getReader() {
		return reader;
	}
	@Override
	public IInjector getInjector() {
		return injector;
	}
	public String toString() {
		return "Timer-"+injector; 
	}
}
