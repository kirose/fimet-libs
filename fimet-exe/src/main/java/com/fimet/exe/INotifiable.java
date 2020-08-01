package com.fimet.exe;

import java.io.File;

import com.fimet.usecase.IRUseCase;

public interface INotifiable {
	void onExecutionFinish(Task task);
	void onEntityUpdated(IRUseCase useCase);
	void onResourceUpdated(File file);
}
