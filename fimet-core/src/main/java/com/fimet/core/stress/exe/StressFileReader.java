package com.fimet.core.stress.exe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.fimet.commons.FimetLogger;
import com.fimet.commons.exception.StressException;
import com.fimet.core.iso8583.adapter.IStreamAdapter;

public class StressFileReader extends Thread implements IReader {
	private AtomicBoolean alive = new AtomicBoolean(false);
	private InputStream in = null;
	private IStreamAdapter adapter;
	private StressInjector injector;
	private byte[] message;
	private File stressFile;
	private InjectorResult result;
	private AtomicInteger numberOfMessagesToRead = new AtomicInteger(0);
	public StressFileReader(StressInjector injector, File stressFile) {
		super("Reader-"+stressFile.getName());
		this.injector = injector;
		this.result = injector.getResult();
		this.stressFile = stressFile;
		this.adapter = injector.getMessenger().getSocket().getAdapter();
		try {
			in = new FileInputStream(stressFile);
		} catch (FileNotFoundException e) {
			throw new StressException(e);
		}
	}
	@Override
	public void read() {
		wakeUp();
	}
	public boolean isWaiting() {
		return getState() == Thread.State.WAITING;
	}
	private void wakeUp() {
		synchronized (this) {
			if (this.getState() == Thread.State.WAITING) {
				this.notify();
			}
		}
	}
	@Override
	public boolean canRead() {
		return alive.get();
	}
	@Override
	public void startRead() {
		this.alive.set(true);
		this.start();
	}
	@Override
	public void stopRead() {
		this.alive.set(false);
		wakeUp();
	}
	public void run() {
		FimetLogger.debug(StressFileReader.class, "Start "+this+" at "+new Timestamp(result.injectorFinishTime));
		result.readerStartTime = System.currentTimeMillis();
		try { 
			while (alive.get()) {
				message = adapter.readStream(in);
				if (message != null && message.length > 0) {
					result.readerMessagesRead++;
					if (numberOfMessagesToRead.get() == 0 || !injector.canEnqueue()) {
						result.readerNumberOfWaits++;
						synchronized (this) {
							this.wait();
						}
					}
					injector.enqueue(message);
					numberOfMessagesToRead.decrementAndGet();
				} else {
					alive.set(false);
				}
			}
		} catch (Exception e) {
			alive.set(false);
			FimetLogger.error("Thread error", e);
		}
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {}
		}
		result.readerFinishTime = System.currentTimeMillis();
		FimetLogger.debug(StressInjector.class, "Finish "+this+" at "+new Timestamp(result.injectorFinishTime));
	}
	@Override
	public void read(int numberOfMessages) {
		this.numberOfMessagesToRead.addAndGet(numberOfMessages);
		wakeUp();
	}
	@Override
	public boolean hasFinish() {
		return !alive.get();
	}
	public String toString(){
		return "Reader-"+stressFile.getName();
	}
}
