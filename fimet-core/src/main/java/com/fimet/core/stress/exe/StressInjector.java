package com.fimet.core.stress.exe;

import java.io.File;
import java.sql.Timestamp;
import java.util.concurrent.LinkedBlockingQueue;

import com.fimet.commons.FimetLogger;
import com.fimet.core.net.IAdaptedSocket;
import com.fimet.core.net.ISocket;
import com.fimet.core.stress.NullInjectorListener;

public class StressInjector extends Thread implements IInjector {
	public static final int MAX_QUEUED = 20;
	public static final int MIN_FOR_AWAKE_READER = 4;
	IAdaptedSocket socket;
	private boolean alive;
	private LinkedBlockingQueue<byte[]> queue;
	private IReader reader;
	private IInjectorListener listener = NullInjectorListener.INSTANCE;
	private long numberOfMessages;
	private long startTime;
	private long finishTime;
	public StressInjector(IAdaptedSocket socket, File stressFile) {
		super("Injector-"+socket);
		this.socket = socket;
		this.queue = new LinkedBlockingQueue<byte[]>();
		this.alive = false;
		this.reader = new StressFileReader(this, stressFile, socket.getAdapter());
		this.reader.startRead();
	}
	public void setListener(IInjectorListener listener) {
		this.listener = listener != null ? listener : NullInjectorListener.INSTANCE;
	}
	public void startInjector() {
		this.alive = true;
		this.start();
	}
	synchronized public void stopInjector() {
		this.alive = true;
		this.notify();
	}
	public void run() {
		try {
			while (alive) {
				byte[] next = queue.take();
				if (queue.isEmpty()) {
					if (reader.canRead()) {
						reader.read();
					} else {
						alive = false;
					}
				} else if (queue.size() <= MIN_FOR_AWAKE_READER) {
					reader.read();
				}
				numberOfMessages++;
				socket.write(next);
			}
		} catch (Exception e) {
			FimetLogger.error("Thread error", e);
		}
		finishTime = System.currentTimeMillis();
		FimetLogger.debug(StressInjector.class, "Finish "+this+" at "+new Timestamp(finishTime));
		listener.onInjectorFinishInject(this);
	}
	@Override
	synchronized public void onReaderStart(IReader reader) {}
	@Override
	synchronized public void onReaderInitilized(IReader reader) {
		FimetLogger.debug(StressInjector.class, "Started "+this+" at "+new Timestamp(System.currentTimeMillis()));
		startTime = System.currentTimeMillis();
		listener.onInjectorStartInject(this);
	}
	@Override
	synchronized public void onReaderFinish(IReader reader) {
//		System.out.println("Reader "+reader
//			+"\nNumber of waits:"+reader.getNumberOfWaits()
//			+"\nMessages readed: "+reader.getNumberOfMessages()
//			+"\nExecution time:"+(reader.getFinishTime()-reader.getStartTime())
//		);
	}
	@Override
	synchronized public void enqueue(byte[] message) {
		queue.add(message);
	}
	@Override
	synchronized public boolean canEnqueue() {
		return queue.size() < MAX_QUEUED;
	}
	public String toString() {
		return "Injector-"+socket;
	}
	@Override
	public ISocket getConnection() {
		return socket.getConnection();
	}
	public long getNumberOfMessages() {
		return numberOfMessages;
	}
	public long getStartTime() {
		return startTime;
	}
	public long getFinishTime() {
		return finishTime;
	}
	
}
