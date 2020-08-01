package com.fimet;

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
import com.fimet.utils.MessageUtils;

import static com.fimet.parser.IMessage.SESSION;

public class SessionManager extends AbstractManager implements ISessionManager {
	private Map<Long, Session> sessions;
	private TimerThread thread;
	public SessionManager() {
		sessions = new ConcurrentHashMap<Long,Session>();
	}
	@Override
	public boolean hasSession(IMessage message) {
		return message != null && message.hasProperty(SESSION);
	}
	@Override
	public Session getSession(IMessage message) {
		if (!sessions.isEmpty() && message != null) {
			if (message.hasProperty(SESSION)) {
				return (Session)message.getProperty(SESSION);
			} else {
				Long key = calculateKey(message);
				if (sessions.containsKey(key)) {
					Session session = sessions.get(key);
					message.setProperty(SESSION, session);
					return session;
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
			useCase.getMessage().setProperty(SESSION, session);
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
					key = calculateKey(next.getUseCase().getMessage());
					//System.out.println("timeout-"+next.useCase.getName()+"-"+new java.sql.Timestamp(System.currentTimeMillis()));
					if (next.getListener() != null) {
						next.getListener().onSessionExpire(next.getUseCase());
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
		return MessageUtils.longHashCode(message);
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
