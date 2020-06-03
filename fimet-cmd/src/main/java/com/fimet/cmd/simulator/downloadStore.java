package com.fimet.cmd.simulator;

import java.io.File;

import com.fimet.cmd.AbstractCommander;
import com.fimet.cmd.Command;
import com.fimet.cmd.Command.Status;


public class downloadStore extends AbstractCommander {
	@Override
	public void doRequest(Command cmd) {
		commandManager.reply(cmd.getId(), Status.OK, new File("store/simulator.db"));
	}
}
