package com.fimet;

import java.util.concurrent.DelayQueue;

import com.fimet.utils.ITimerListener;
import com.fimet.utils.Scheduled;

public class TimerManager extends AbstractManager implements ITimerManager {
	private DelayQueue<Scheduled> queue = new DelayQueue<Scheduled>();
	private TimerThread thread;
	public TimerManager() {
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
				if (s.getObject() == object)
					match = s;
			}
			if (match != null) {
				queue.remove(match);
			}
			}
		}
	}
	private class TimerThread extends Thread {
		private boolean alive = true;
		public TimerThread() {
			super("TimerThread");
		}
		public void run() {
			try {
				Scheduled next;
				while (alive) {
					next = queue.take();
					if (next != null && next.getListener() != null) {
						next.getListener().onTimeout(next.getObject());
					}
				}
			} catch (Exception e) {
				FimetLogger.error("Thread error", e);
				run();
			}
		}
	}
	@Override
	public void start() {
		if (thread == null) {
			thread = new TimerThread();
			thread.start();
		}
	}
	@Override
	public void reload() {
		queue.clear();
		thread.alive = false;
		thread.notifyAll();
		thread = null;
		start();
	}

}