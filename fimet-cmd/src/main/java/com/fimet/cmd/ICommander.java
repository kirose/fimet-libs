package com.fimet.cmd;

import java.util.UUID;

import com.fimet.cmd.Command.Status;

public interface ICommander {
	void doRequest(Command cmd);
	Object doResponse(UUID idCommand, Status status, Object exeResult);
}
