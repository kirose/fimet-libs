package com.fimet.exe;

import java.sql.Timestamp;

import org.slf4j.LoggerFactory;import org.slf4j.Logger;

import com.fimet.exe.stress.IInjector;
import com.fimet.exe.stress.IReader;
import com.fimet.exe.stress.ITimer;

public class StressTimer extends Thread implements ITimer {
	private static Logger logger = LoggerFactory.getLogger(StressTimer.class);
	private StressInjector injector;
	private StressFileReader reader;
	private int cycleTime;
	private int messagesPerCycle;
	private boolean alive;
	public StressTimer(StressInjector injector, StressFileReader reader, int cycleTime, int messagesPerCycle) {
		super("StressTimer"+injector);
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
		logger.debug("Start "+this+" at "+new Timestamp(System.currentTimeMillis()));
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
			logger.debug("Timer Error ",e);
		}
		logger.debug("Finish "+this+" at "+new Timestamp(System.currentTimeMillis()));
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
