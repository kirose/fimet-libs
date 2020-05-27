package com.fimet.usecase;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;

import com.fimet.AbstractManager;
import com.fimet.FimetLogger;
import com.fimet.ISessionManager;
import com.fimet.parser.IMessage;
import com.fimet.usecase.ISessionListener;
import com.fimet.usecase.IUseCase;
import com.fimet.usecase.Session;

public class SessionManager extends AbstractManager implements ISessionManager {
	private Map<Long, Session> sessions;
	private TimerThread thread;
	public SessionManager() {
		sessions = new ConcurrentHashMap<Long,Session>();
	}
	@Override
	public Session getSession(IMessage message) {
		if (!sessions.isEmpty() && message != null) {
			if (message.hasProperty("session")) {
				return (Session)message.getProperty("session");
			} else {
				Long key = calculateKey(message);
				//System.out.println("ASSIGN:"+key);
				if (sessions.containsKey(key)) {
					return sessions.get(key);
				}
			}
		}
		return null;
	}
	@Override
	public Session createSession(IUseCase useCase, ISessionListener listener) {
		if (useCase != null) {
			Session session = new Session(useCase, listener);
			thread.queue.add(session);
			Long key = calculateKey(useCase.getMessage());
			useCase.getMessage().setProperty("session", session);
			sessions.put(key, session);
			return session;
		}
		return null;
	}
	@Override
	public Session createSession(IUseCase useCase) {
		return createSession(useCase, null);
	}
	@Override
	public void deleteSession(IUseCase useCase) {
		if (useCase != null && useCase.getMessage() != null) {
			Long key = calculateKey(useCase.getMessage());
			Session msg = sessions.get(key);
			thread.queue.remove(msg);
			sessions.remove(key);
		}
	}
	class TimerThread extends Thread {
		private DelayQueue<Session> queue = new DelayQueue<Session>();
		private boolean alive = true;
		public TimerThread() {
			super("SessionThread");
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
					//System.out.println("timeout-"+next.useCase.getName()+"-"+new java.sql.Timestamp(System.currentTimeMillis()));
					if (next.listener != null) {
						next.listener.onSessionExpire(next.useCase);
					}
					sessions.remove(key);
				}
			} catch (Exception e) {
				FimetLogger.error("Thread error", e);
				run();
			}
		}
	}

	public Long calculateKey(IMessage message) {
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
	public void start() {
		thread = new TimerThread();
		thread.start();
	}
	@Override
	public void reload() {
		thread.alive = false;
		thread.notifyAll();
		thread = null;
		start();
	}
}
