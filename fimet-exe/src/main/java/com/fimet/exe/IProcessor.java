package com.fimet.exe;

import com.fimet.usecase.IUseCase;

public interface IProcessor {
	void process(IUseCase useCase);
	void setListener(IUseCaseProcessorListener listener); 
}
