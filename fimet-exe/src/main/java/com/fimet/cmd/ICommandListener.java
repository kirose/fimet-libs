package com.fimet.cmd;

import java.util.UUID;

import com.fimet.cmd.Command.Status;

public interface ICommandListener {
	void suggestReply(UUID idCommand, Status status, Object response);
	void reply(UUID idCommand, Status status, Object response);
}
