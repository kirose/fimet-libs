package com.fimet.server;

public interface ICommandServer extends ICommandListener {
	void setCommandManager(ICommandManager commandManager);
	void start();
}
