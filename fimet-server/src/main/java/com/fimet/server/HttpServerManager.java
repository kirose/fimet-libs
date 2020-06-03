package com.fimet.server;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.fimet.FimetLogger;
import com.fimet.ICommandManager;
import com.fimet.IServerManager;
import com.fimet.ITimerManager;
import com.fimet.Manager;
import com.fimet.Version;
import com.fimet.cmd.Command;
import com.fimet.cmd.Command.Status;
import com.fimet.cmd.ICommandListener;
import com.fimet.utils.ITimerListener;
import com.fimet.utils.Scheduled;

public class HttpServerManager implements IServerManager, ITimerListener, ICommandListener {
	HttpSocketPool pool;
	ICommandManager commandManager = Manager.get(ICommandManager.class);
	static ITimerManager timerManager = Manager.get(ITimerManager.class);
	static long MAX_WAITING_RESPONSE = Manager.getPropertyLong("command.server.requestTimeout", 10000L);

	Map<UUID, Scheduled> schedule;

	public HttpServerManager() {
		schedule = new HashMap<UUID, Scheduled>();
		commandManager.setCommandListener(this);
	}

	@Override
	public void start() {
		pool = new HttpSocketPool(this);
	}

	private boolean isValidProductKey(HttpMessage message) {
		String productKey = message.getHeader("product-key");
		return Version.isValidProductKey(productKey);
	}

	public void onHttpRequest(HttpMessage message) {
		FimetLogger.debug(HttpServerManager.class, "Incoming Message:" + message);
		if (isValidProductKey(message)) {
			Command cmd = new Command(message.getId());
			Scheduled scheduled = timerManager.schedule(message, MAX_WAITING_RESPONSE, this);
			schedule.put(message.getId(), scheduled);
			cmd.setCommand(message.getUrl());
			cmd.setParams(message.getParams());
			cmd.setData(message.getBody());
			commandManager.execute(cmd);
		} else {
			FimetLogger.error(HttpServerManager.class, "Bad Request: " + message);
			message.setCode(HttpCode.BAD_REQUEST);
			message.setResponse("{\"error\":\"ProductKey Expired\"}");
			message.getSocket().reply(message);
		}
	}

	@Override
	public void onTimeout(Object o) {
		HttpMessage m = (HttpMessage) o;
		if (schedule.containsKey(m.getId())) {
			schedule.remove(m.getId());
			if (m.getResponse() == null) {
				m.setCode(HttpCode.REQUEST_TIMEOUT);
				m.setResponse("{\"error\":\"Timeout\"}".getBytes());
				m.getSocket().reply(m);
			} else {
				m.getSocket().reply(m);
			}
		}
	}
	@Override
	public void suggestReply(UUID idCommand, Status status, Object response) {
		Scheduled scheduled = schedule.get(idCommand);
		if (scheduled != null) {
			HttpMessage m = (HttpMessage) scheduled.getObject();
			if (status == Status.OK) {
				m.setCode(HttpCode.OK);
			} else {
				m.setCode(HttpCode.BAD_REQUEST);
			}
			m.setResponse(response);
		}
	}
	@Override
	public void reply(UUID idCommand, Status status, Object response) {
		Scheduled scheduled = schedule.remove(idCommand);
		if (scheduled != null) {
			scheduled.cancel();
			HttpMessage m = (HttpMessage) scheduled.getObject();
			if (status == Status.OK) {
				m.setCode(HttpCode.OK);
			} else {
				m.setCode(HttpCode.BAD_REQUEST);
			}
			m.setResponse(response);
			m.getSocket().reply(m);
		}
	}

	@Override
	public void reload() {}

	@Override
	public void saveState() {}
}
