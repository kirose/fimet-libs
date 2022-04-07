package com.fimet;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;

import javax.annotation.PostConstruct;

import org.slf4j.LoggerFactory;import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.fimet.AbstractManager;

import com.fimet.ISessionManager;
import com.fimet.parser.IMessage;
import com.fimet.usecase.ISessionListener;
import com.fimet.usecase.IUseCase;
import com.fimet.usecase.Session;

import static com.fimet.parser.IMessage.SESSION;
@Component
public class SessionManager extends AbstractManager implements ISessionManager {
	private static Logger logger = LoggerFactory.getLogger(SessionManager.class);
	private Map<Long, Session> sessions;
	private TimerThread thread;
	private String[] pk;
	public SessionManager() {
		sessions = new ConcurrentHashMap<Long,Session>();
		String pk = Manager.getProperty("session.primarykey","37");
		this.pk = pk.split(",");
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
				logger.error("Thread error", e);
				run();
			}
		}
	}

	public Long calculateKey(IMessage message) {
		return longHashCode(message);
	}
	@PostConstruct
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
	@Override
	public void stop() {}
	private long longHashCode(IMessage message) {
		if (message != null) {
			//String stan = message.getValue(11);
			//String tdt = message.getValue(7);//Transmission date and time
			//String time = message.getValue(12);//Transmission time
			//String rrn = message.getValue(37);
			final int prime = 31;
			long key = 1;
			for (String id : pk) {
				String value = message.getValue(id);
				key = prime * key + ((value == null) ? 0 : value.hashCode());
			}
			//key = prime * key + ((stan == null) ? 0 : stan.hashCode());
			//key = prime * key + ((tdt == null) ? 0 : tdt.hashCode());
			//key = prime * key + ((time == null) ? 0 : time.hashCode());
			//key = prime * key + ((rrn == null) ? 0 : rrn.hashCode());
			return key;
		} else {
			return 0L;
		}
	}
}
