package com.fimet.cmd;

import java.util.UUID;

import com.fimet.ICommandManager;
import com.fimet.Manager;
import com.fimet.cmd.Command.Status;

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
