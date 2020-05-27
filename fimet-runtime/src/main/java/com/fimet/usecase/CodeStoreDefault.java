package com.fimet.usecase;

import java.util.HashMap;
import java.util.Map;

import com.fimet.entity.ECodeStore;

public class CodeStoreDefault implements ICodeStore {
	Map<String, ECodeStore> store = new HashMap<String, ECodeStore>();
	@Override
	public void store(String useCaseName, String authCode, String responseCode) {
		store.put(useCaseName, new ECodeStore(useCaseName, authCode, responseCode));
	}
	@Override
	public ECodeStore get(String usecaseName) {
		return store.get(usecaseName);
	}
}
