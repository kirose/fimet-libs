package com.fimet.usecase;

import java.sql.Timestamp;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;

import com.fimet.ISessionManager;
import com.fimet.commons.FimetLogger;
import com.fimet.iso8583.parser.Message;
import com.fimet.usecase.exe.ISessionListener;

public class SessionManager implements ISessionManager {
	private Map<Long, Session> contexts;
	private TimerThread timer;
	public SessionManager() {
		contexts = new ConcurrentHashMap<Long,Session>();
		timer = new TimerThread();
		timer.start();
	}
	@Override
	public Session getSession(Message message) {
		if (!contexts.isEmpty() && message != null) {
			Long key = calculateKey(message);
			//System.out.println("ASSIGN:"+key);
			if (contexts.containsKey(key)) {
				return contexts.get(key);
			}
		}
		return null;
	}
	@Override
	public Session createSession(UseCase useCase, ISessionListener listener) {
		if (useCase != null) {
			Session session = new Session(useCase, listener);
			timer.queue.add(session);
			Long key = calculateKey(useCase.getMessage());
			contexts.put(key, session);
			return session;
		}
		return null;
	}

	@Override
	public Session createSession(UseCase useCase) {
		return createSession(useCase, null);
	}
	public void deleteSession(UseCase useCase) {
		if (useCase != null && useCase.getMessage() != null) {
			Long key = calculateKey(useCase.getMessage());
			Session msg = contexts.get(key);
			timer.queue.remove(msg);
			contexts.remove(key);
		}
	}
	class TimerThread extends Thread {
		private DelayQueue<Session> queue = new DelayQueue<Session>();
		private boolean alive = true;
		public TimerThread() {
		}
		public void startTimer() {
			this.start();
		}
		public void stopTimer() {
			queue.clear();
			alive = false;
			this.notifyAll();
		}
		public void run() {
			try {
				Session next;
				Long key;
				while (alive) {
					next = queue.take();
					key = calculateKey(next.useCase.getMessage());
					System.out.println("timeout-"+next.useCase.getName()+"-"+new Timestamp(System.currentTimeMillis()));
					if (next.listener != null) {
						next.listener.timeout(next.useCase);
					}
					contexts.remove(key);
				}
			} catch (Exception e) {
				FimetLogger.error("Thread error", e);
				run();
			}
		}
	}

	public Long calculateKey(Message message) {
		if (message != null) {
			String stan = message.getValue(11);
			String rrn = message.getValue(37);
			//String adata = message.getValue(48);
			final int prime = 31;
			long key = 1;
			key = prime * key + ((stan == null) ? 0 : stan.hashCode());
			key = prime * key + ((rrn == null) ? 0 : rrn.hashCode());
			//key = prime * key + ((adata == null) ? 0 : adata.hashCode());
			//System.out.println("KEY:"+key);
			return key;
		} else {
			return 0L;
		}
	}
	@Override
	public void free() {
		
	}
	@Override
	public void saveState() {
		
	}
}
