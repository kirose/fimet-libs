package com.fimet.server;

import java.util.UUID;

import com.fimet.Manager;
import com.fimet.server.Command.Status;

public class AbstractCommander implements ICommander {
	protected ICommandManager commandManager = Manager.get(ICommandManager.class); 
	public void doRequest(Command cmd) {
		commandManager.reply(cmd.getId(),Status.ERROR,"{\"error\":\"Invalid Command\"}");
	}
	@Override
	public Object doResponse(UUID idCommand, Status status, Object response) {
		return null;
	}
}
