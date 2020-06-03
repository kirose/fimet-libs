package com.fimet.cmd.fimet;

import java.io.File;

import com.fimet.cmd.AbstractCommander;
import com.fimet.cmd.Command;
import com.fimet.cmd.Command.Status;


public class downloadLog extends AbstractCommander {
	@Override
	public void doRequest(Command cmd) {
		commandManager.reply(cmd.getId(), Status.OK, new File("logs/fimet.log"));
	}
}
