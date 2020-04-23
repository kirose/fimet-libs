package com.fimet.core.stress.exe;

import java.io.File;
import java.sql.Timestamp;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.fimet.commons.FimetLogger;
import com.fimet.core.net.IAdaptedSocket;
import com.fimet.core.net.IMessenger;

public class StressInjector extends Thread implements IInjector {
	public static final int MAX_QUEUE_SIZE = 20;
	public static final int MIN_SIZE_FOR_WAKE_UP_READER = 4;
	private IAdaptedSocket socket;
	private AtomicBoolean alive = new AtomicBoolean(false);
	private ConcurrentLinkedQueue<byte[]> queue;
	StressFileReader reader;
	StressTimer timer;
	IMessenger messenger;
	InjectorResult result;
	MessengerResult messengerResult;
	AtomicInteger numberOfMessagesToInject = new AtomicInteger(0);
	IInjectorListener listener;
	public StressInjector(IMessenger messenger, MessengerResult messengerResult, File stressFile, int cycleTime, int messagesPerCycle) {
		super("Injector-"+messenger.getConnection());
		this.messengerResult = messengerResult;
		this.messenger = messenger;
		this.socket = messenger.getSocket();
		this.queue = new ConcurrentLinkedQueue<byte[]>();
		this.result = new InjectorResult("Injector-"+messenger.getConnection());
		this.reader = new StressFileReader(this, stressFile);
		this.timer = new StressTimer(this, reader, cycleTime, messagesPerCycle);
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
							if (reader.hasFinish()) {
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
	public IMessenger getMessenger() {
		return messenger;
	}
	@Override
	public boolean isFinished() {
		return queue.isEmpty() && !reader.canRead();
	}
	public void setReader(StressFileReader reader) {
		this.reader = reader;
	}
	public boolean hasFinish() {
		return queue.isEmpty();
	}
	public void setListener(IInjectorListener listener) {
		this.listener = listener!= null ? listener : NullInjectorListener.INSTANCE; 
	}
	public InjectorResult getResult() {
		return result;
	}
	public MessengerResult getMessengerResult() {
		return messengerResult;
	}
	public String toString() {
		return "Injector-"+socket;
	}
	public String getStatsCycle() {
		return "[TS:"+new Timestamp(System.currentTimeMillis())
		+",N:"+messengerResult.getNumOfCycle()
		+",IP:"+socket.getConnection().getAddress()
		+",P:"+socket.getConnection().getPort()
		+","+result.getStatsCycle()
		+","+messengerResult.getStatsCycle()+"]";
	}
	public String getStatsGlobal() {
		return "[Timestamp:"+new Timestamp(System.currentTimeMillis())
		+",NumCycle:"+messengerResult.getNumOfCycle()
		+",IP:"+socket.getConnection().getAddress()
		+",Port:"+socket.getConnection().getPort()
		+","+result.getStatsCycle()
		+","+messengerResult.getStatsCycle()+"]";
	}
}
