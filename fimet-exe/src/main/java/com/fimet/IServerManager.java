package com.fimet;

import java.util.UUID;

import com.fimet.cmd.Command.Status;

/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface IServerManager extends IManager {
	void stop();
	void suggestReply(UUID idCommand, Status status, Object response);
	void reply(UUID idCommand, Status status, Object response);
}
