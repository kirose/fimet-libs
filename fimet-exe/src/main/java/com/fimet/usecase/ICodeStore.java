package com.fimet.usecase;

import com.fimet.IManager;

public interface ICodeStore extends IManager {
	void store(IUseCase usecase);
	ICode get(String useCaseName);
}
