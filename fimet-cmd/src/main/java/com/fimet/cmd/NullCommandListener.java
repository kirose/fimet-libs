package com.fimet.cmd;

import java.util.UUID;

import com.fimet.cmd.Command.Status;

public class NullCommandListener implements ICommandListener {
	public static final ICommandListener INSTANCE = new NullCommandListener();
	public NullCommandListener() {}

	@Override
	public void suggestReply(UUID idCommand, Status status, Object response) {}

	@Override
	public void reply(UUID idCommand, Status status, Object response) {}

}
