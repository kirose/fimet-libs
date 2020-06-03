package com.fimet.usecase;


public interface ICodeStore {
	void store(String useCaseName, String authCode, String responseCode);
	ICode get(String useCaseName);
}
