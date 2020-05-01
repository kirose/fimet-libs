package com.fimet.timer;

import java.util.concurrent.DelayQueue;

import com.fimet.ITimerManager;
import com.fimet.commons.FimetLogger;

public class TimerManager extends Thread implements ITimerManager {
	private DelayQueue<Scheduled> queue = new DelayQueue<Scheduled>();
	private boolean alive = true;
	public TimerManager() {
		this.start();
	}
	public void stopTimer() {
		queue.clear();
		alive = false;
		this.notifyAll();
	}
	@Override
	public Scheduled schedule(Object object, long time, ITimerListener listener) {
		Scheduled scheduled = new Scheduled(object, time, listener);
		queue.add(scheduled);
		return scheduled;
	}
	@Override
	public void cancel(Object object) {
		if (!queue.isEmpty()) {
			if (object instanceof Scheduled) {
				queue.remove((Scheduled)object);
			} else {
			Scheduled match = null;
			for (Scheduled s : queue) {
				if (s.object == object)
					match = s;
			}
			if (match != null) {
				queue.remove(match);
			}
			}
		}
	}
	public void run() {
		try {
			Scheduled next;
			while (alive) {
				next = queue.take();
				if (next.listener != null) {
					next.listener.onTimeout(next.object);
				}
			}
		} catch (Exception e) {
			FimetLogger.error("Thread error", e);
			run();
		}
	}
	@Override
	public void free() {
	}
	@Override
	public void saveState() {
	}
}