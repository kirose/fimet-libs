package com.fimet.exe;

import com.fimet.usecase.IUseCase;

public interface IUseCaseIterator {
	boolean hasNext();
	IUseCase next();
}
