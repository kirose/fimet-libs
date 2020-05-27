package com.fimet.server;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.fimet.IExecutorManager;
import com.fimet.Manager;
import com.fimet.exe.IExecutorListener;
import com.fimet.exe.Task;
import com.fimet.server.Command.Status;

public class CommandManager implements ICommandManager, IExecutorListener {
	private ICommandServer server;
	private IExecutorManager executorManager = Manager.get(IExecutorManager.class);
	private Map<UUID, ICommander> mapCmdCommander = new HashMap<UUID, ICommander>();
	private Map<String, ICommander> mapUrlCommander = new HashMap<String, ICommander>();
	public CommandManager() {
	}
	@Override
	public void start() {
		executorManager.setExecutorListener(this);
		server = Manager.getExtension(ICommandServer.class, HttpCommandServer.class);
		server.setCommandManager(this);
		server.start();
	}
	@Override
	public void execute(Command cmd) {
		if (mapUrlCommander.containsKey(cmd.getCommand())) {
			ICommander commander = mapUrlCommander.get(cmd.getCommand());
			mapCmdCommander.put(cmd.getId(), commander);
			commander.doRequest(cmd);
		} else {
			ICommander commander = urlToCommander(cmd.getCommand());
			if (commander == null) {
				server.reply(cmd.getId(),Status.ERROR,"{\"error\":\"Invalid command\"}");
			} else {
				mapUrlCommander.put(cmd.getCommand(), commander);
				mapCmdCommander.put(cmd.getId(), commander);
				commander.doRequest(cmd);
			}
		}
	}
	private ICommander urlToCommander(String url) {
		try {
			String className = url.replace('/', '.');
			Class<?> clazz = Class.forName("com.fimet.server"+className);
			return (ICommander)clazz.newInstance();
		} catch (Exception e) {
			return null;
		}
	}
	@Override
	public void reload() {}
	@Override
	public void saveState() {}
	@Override
	public ICommandServer getServer() {
		return server;
	}
	@Override
	public void reply(UUID idCommand, Status status, Object response) {
		mapCmdCommander.remove(idCommand);
		server.reply(idCommand, status, response);
	}
	@Override
	public void onTaskFinish(Task task) {
		if (task.getId() != null) {
			ICommander commander = mapCmdCommander.remove(task.getId());
			if (commander != null) {
				Object response = commander.doResponse(task.getId(), Status.OK, task);
				reply(task.getId(), Status.OK, response);
			}
		}		
	}
}
