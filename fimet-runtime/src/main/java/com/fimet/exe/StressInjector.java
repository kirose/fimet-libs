package com.fimet.exe;

import java.io.File;
import java.sql.Timestamp;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.fimet.FimetLogger;
import com.fimet.exe.stress.IInjector;
import com.fimet.exe.stress.IInjectorListener;
import com.fimet.exe.stress.NullInjectorListener;
import com.fimet.simulator.ISimulator;
import com.fimet.socket.ISocket;
import com.fimet.stress.Stress;

public class StressInjector extends Thread implements IInjector {
	public static final int MAX_QUEUE_SIZE = 20;
	public static final int MIN_SIZE_FOR_WAKE_UP_READER = 4;
	private ISocket socket;
	private ISimulator simulator;
	private AtomicBoolean alive = new AtomicBoolean(false);
	private ConcurrentLinkedQueue<byte[]> queue;
	StressFileReader reader;
	StressTimer timer;
	InjectorResult result;
	AtomicInteger numberOfMessagesToInject = new AtomicInteger(0);
	IInjectorListener listener;
	public StressInjector(Stress stress, ISimulator simulator, File stressFile) {
		super("Injector-"+simulator.getSocket());
		this.simulator = simulator; 
		this.socket = simulator.getSocket();
		this.queue = new ConcurrentLinkedQueue<byte[]>();
		this.result = new InjectorResult(socket.getParameters());
		this.result.cycleTime = stress.getCycleTime();
		this.result.name = stress.getName();
		this.result.initialReadSocket.set(socket.getNumOfRead());
		this.result.initialWriteSocket.set(socket.getNumOfWrite());
		this.result.initialApprovalsSimulator.set(simulator.getNumOfApprovals());
		this.reader = new StressFileReader(this, stressFile);
		this.timer = new StressTimer(this, reader, stress.getCycleTime(), stress.getMessagesPerCycle());
	}
	public void startInjector() {
		FimetLogger.debug(StressInjector.class, "Start Thread "+this+" at "+new Timestamp(System.currentTimeMillis()));		
		this.alive.set(true);
		this.start();
		this.reader.startRead();
		this.timer.startTimer();
	}
	public void stopInjector() {
		this.alive.set(false);
		this.timer.stopTimer();
		this.reader.stopRead();
	}
	public void inject(int numberOfMessages) {
		this.numberOfMessagesToInject.addAndGet(numberOfMessages);
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
		FimetLogger.debug(StressInjector.class, "Start "+this+" at "+new Timestamp(System.currentTimeMillis()));
		result.injectorStartTime = System.currentTimeMillis();
		listener.onInjectorStartInject(this);
		try {
			while (alive.get()) {
				if(numberOfMessagesToInject.get() > 0) {
					if (queue.isEmpty()) {
						if (reader.canRead()) {
							reader.read();
						} else {
							alive.set(false);
						}
					} else {
						byte[] next = queue.poll();
						result.injectorMessagesInjectedCycle.incrementAndGet();
						numberOfMessagesToInject.decrementAndGet();
						socket.write(next);
						if (reader.isWaiting() && queue.size() <= MIN_SIZE_FOR_WAKE_UP_READER) {
							reader.read();
						}
						if (numberOfMessagesToInject.get() == 0) {
							result.injectorMessagesInjected.addAndGet(result.injectorMessagesInjectedCycle.get());
							result.injectorFinishCycleTime.set(System.currentTimeMillis());
							listener.onInjectorFinishCycle(this);
							result.injectorMessagesInjectedCycle.set(0);
							if (hasFinish()) {
								alive.set(false);
							}
						}
						
					}
				} else {
					synchronized (this) {
						this.wait();
					}
				}
			}
		} catch (Exception e) {
			FimetLogger.error("Thread error", e);
		}
		result.injectorFinishTime = System.currentTimeMillis();
		FimetLogger.debug(StressInjector.class, "Finish "+this+" at "+new Timestamp(result.injectorFinishTime));
		listener.onInjectorFinishInject(this);
	}
	public void enqueue(byte[] message) {
		queue.add(message);
		wakeUp();
	}
	public boolean canEnqueue() {
		return queue.size() <= MAX_QUEUE_SIZE;
	}
	public ISimulator getSimulator() {
		return simulator;
	}
	public ISocket getSocket() {
		return socket;
	}
	public void setReader(StressFileReader reader) {
		this.reader = reader;
	}
	public boolean hasFinish() {
		return queue.isEmpty() && reader.hasFinish();
	}
	public void setListener(IInjectorListener listener) {
		this.listener = listener!= null ? listener : NullInjectorListener.INSTANCE; 
	}
	public InjectorResult getResult() {
		return result;
	}
	public String toString() {
		return "Injector-"+socket;
	}
}
