package com.fimet.server;

import java.util.UUID;

import com.fimet.server.Command.Status;

public interface ICommander {
	void doRequest(Command cmd);
	Object doResponse(UUID idCommand, Status status, Object exeResult);
}
