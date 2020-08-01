package com.fimet.exe;

public interface IStressProcessorListener {
	void onUseCaseProcessorStart(IUseCaseProcessor processor);
	void onUseCaseProcessorFinish(IUseCaseProcessor processor);
}
