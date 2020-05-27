package com.fimet.server.fimet;

import java.io.File;

import com.fimet.server.AbstractCommander;
import com.fimet.server.Command;
import com.fimet.server.Command.Status;


public class downloadLog extends AbstractCommander {
	@Override
	public void doRequest(Command cmd) {
		commandManager.reply(cmd.getId(), Status.OK, new File("logs/fimet.log"));
	}
}
