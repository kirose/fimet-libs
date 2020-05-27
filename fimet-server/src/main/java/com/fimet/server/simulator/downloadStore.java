package com.fimet.server.simulator;

import java.io.File;

import com.fimet.server.AbstractCommander;
import com.fimet.server.Command;
import com.fimet.server.Command.Status;


public class downloadStore extends AbstractCommander {
	@Override
	public void doRequest(Command cmd) {
		commandManager.reply(cmd.getId(), Status.OK, new File("store/simulator.db"));
	}
}
