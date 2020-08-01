package com.fimet.usecase;

import com.fimet.IManager;

public interface ICodeStore extends IManager {
	void store(String useCaseName, String authCode, String responseCode);
	ICode get(String useCaseName);
}
