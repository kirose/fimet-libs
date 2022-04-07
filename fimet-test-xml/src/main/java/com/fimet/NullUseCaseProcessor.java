package com.fimet;

import org.slf4j.LoggerFactory;import org.slf4j.Logger;

import com.fimet.exe.IUseCaseProcessor;
import com.fimet.exe.UseCaseTask;
import com.fimet.usecase.IUseCase;
import com.fimet.utils.ThreadUtils;

public class NullUseCaseProcessor implements IUseCaseProcessor {
	private static Logger logger = LoggerFactory.getLogger(NullUseCaseProcessor.class);
	@Override
	public void process(UseCaseTask task, IUseCase useCase) {
		ThreadUtils.runAsync(()->{
			task.finishProcessor();
			logger.debug("Finish Processor!!");
		});
	}

}
