package com.fimet.cmd;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.fimet.ICommandManager;
import com.fimet.IExecutorManager;
import com.fimet.Manager;
import com.fimet.cmd.Command.Status;
import com.fimet.exe.IExecutorListener;
import com.fimet.exe.Task;

public class CommandManager implements ICommandManager, IExecutorListener {
	private IExecutorManager executorManager;
	private Map<UUID, ICommander> mapCmdCommander;
	private Map<String, ICommander> mapUrlCommander;
	private ICommandListener listener;
	public CommandManager() {
		executorManager = Manager.get(IExecutorManager.class);
		mapCmdCommander = new HashMap<UUID, ICommander>();
		mapUrlCommander = new HashMap<String, ICommander>();
		listener = NullCommandListener.INSTANCE;
	}
	@Override
	public void start() {
		executorManager.setExecutorListener(this);
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
				listener.reply(cmd.getId(),Status.ERROR,"{\"error\":\"Invalid command\"}");
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
			Class<?> clazz = Class.forName("com.fimet.cmd"+className);
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
	public void reply(UUID idCommand, Status status, Object response) {
		mapCmdCommander.remove(idCommand);
		listener.reply(idCommand, status, response);
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
	@Override
	public void suggestReply(UUID idCommand, Status status, Object response) {
		listener.suggestReply(idCommand, status, response);
	}
	@Override
	public void setCommandListener(ICommandListener listener) {
		listener = listener!=null?listener:NullCommandListener.INSTANCE;		
	}
}
