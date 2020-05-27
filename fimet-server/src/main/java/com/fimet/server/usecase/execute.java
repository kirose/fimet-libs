package com.fimet.server.usecase;

import java.util.UUID;

import com.fimet.FimetLogger;
import com.fimet.IExecutorManager;
import com.fimet.Manager;
import com.fimet.exe.Task;
import com.fimet.server.AbstractCommander;
import com.fimet.server.Command;
import com.fimet.server.Command.Status;
import com.fimet.usecase.UseCase;
import com.fimet.utils.JsonUtils;
import com.fimet.utils.UseCaseUtils;

public class execute extends AbstractCommander {
	IExecutorManager executorManager = Manager.get(IExecutorManager.class);
	@Override
	public void doRequest(Command cmd) {
		if (cmd.getData() == null) {
			commandManager.reply(cmd.getId(), Status.ERROR,"{\"error\":\"UseCase is null\"}");
		} else {
			try {
				UseCase useCase = UseCaseUtils.parseForExecutionFromJson(new String(cmd.getData()));
				executorManager.execute(new Task(cmd.getId(), useCase));
			} catch (Exception e) {
				FimetLogger.error(getClass(), "UseCaseCommand error "+cmd.getCommand(),e);
				commandManager.reply(cmd.getId(), Status.ERROR,"{\"error\":\"Invalid UseCase\"}");
			}
		}		
	}

	@Override
	public Object doResponse(UUID idCommand, Status status, Object data) {
		Task task = (Task)data;
		return JsonUtils.toJson(task.getResult());
	}

}
