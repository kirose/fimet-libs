package com.fimet;

import java.util.UUID;

import com.fimet.cmd.Command;
import com.fimet.cmd.Command.Status;
import com.fimet.cmd.ICommandListener;

/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface ICommandManager extends IManager {
	void execute(Command cmd);
	void reply(UUID idCommand, Status status, Object response);
	void suggestReply(UUID idCommand, Status status, Object response);
	void setCommandListener(ICommandListener listener);
}
