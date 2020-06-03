package com.fimet.cmd.usecase;

import java.util.UUID;

import com.fimet.FimetLogger;
import com.fimet.IExecutorManager;
import com.fimet.Manager;
import com.fimet.cmd.AbstractCommander;
import com.fimet.cmd.Command;
import com.fimet.cmd.Command.Status;
import com.fimet.exe.Task;
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
				UseCase useCase = UseCaseUtils.fromJson(new String(cmd.getData()));
				Task task = executorManager.execute(new Task(cmd.getId(), useCase));
				commandManager.suggestReply(cmd.getId(), Command.Status.OK, "{\"idTask\":\""+task.getId()+"\"}");
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
