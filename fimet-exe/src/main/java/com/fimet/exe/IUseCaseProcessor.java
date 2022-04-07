package com.fimet.exe;

import com.fimet.IExtension;
import com.fimet.usecase.IUseCase;

public interface IUseCaseProcessor extends IExtension {
	void process(UseCaseTask task, IUseCase useCase);
}
