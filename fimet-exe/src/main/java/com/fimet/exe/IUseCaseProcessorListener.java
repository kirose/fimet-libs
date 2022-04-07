package com.fimet.exe;

public interface IUseCaseProcessorListener {
	void onUseCaseProcessorStart(IUseCaseProcessor processor);
	void onUseCaseProcessorFinish(IUseCaseProcessor processor);
}
