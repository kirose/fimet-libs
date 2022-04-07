package com.fimet.exe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.LoggerFactory;import org.slf4j.Logger;

import com.fimet.exe.stress.IReader;
import com.fimet.parser.IStreamAdapter;
import com.fimet.stress.StressException;

public class StressFileReader extends Thread implements IReader {
	private static Logger logger = LoggerFactory.getLogger(StressFileReader.class);
	private AtomicBoolean alive = new AtomicBoolean(false);
	private InputStream in = null;
	private IStreamAdapter adapter;
	private StressInjector injector;
	private byte[] message;
	private File stressFile;
	private SocketResult result;
	private AtomicInteger numberOfMessagesToRead = new AtomicInteger(0);
	public StressFileReader(StressInjector injector, File stressFile) {
		super("Reader-"+stressFile.getName());
		this.injector = injector;
		this.result = injector.getResult();
		this.stressFile = stressFile;
		this.adapter = injector.getSocket().getAdapter();
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
		logger.debug("Start "+this+" at "+new Timestamp(result.injectorFinishTime));
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
					in.read();// New Line
				} else {
					alive.set(false);
				}
			}
		} catch (Exception e) {
			alive.set(false);
			logger.error("Thread error", e);
		}
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {}
		}
		result.readerFinishTime = System.currentTimeMillis();
		logger.debug("Finish "+this+" at "+new Timestamp(result.injectorFinishTime));
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
