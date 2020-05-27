package com.fimet.usecase;

import com.fimet.entity.ECodeStore;

public interface ICodeStore {
	void store(String useCaseName, String authCode, String responseCode);
	ECodeStore get(String useCaseName);
}
