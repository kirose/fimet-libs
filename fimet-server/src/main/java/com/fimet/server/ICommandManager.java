package com.fimet.server;

import com.fimet.IManager;

public interface ICommandManager extends IManager, ICommandListener {
	void execute(Command cmd);
	ICommandServer getServer();
}
