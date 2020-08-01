package com.fimet.usecase;

import java.util.HashMap;
import java.util.Map;

import com.fimet.Manager;

public class CodeStoreDefault implements ICodeStore {
	private ECodeStore[] tmp;
	private Map<String,Integer> map;
	private int index;
	private static final int MAX_TMP_SIZE = Manager.getPropertyInteger("codestore.maxSize", 15);
	public CodeStoreDefault() {
		index = -1;
		tmp = new ECodeStore[MAX_TMP_SIZE];
		map = new HashMap<>();
	}
	@Override
	public void store(String useCaseName, String authCode, String responseCode) {
		ECodeStore e = new ECodeStore();
		e.setUseCaseName(useCaseName);
		e.setAuthCode(authCode);
		e.setResponseCode(responseCode);
		index = (index+1)%MAX_TMP_SIZE;
		if (tmp[index] != null) {
			map.remove(tmp[index].getUseCaseName());
		}
		map.put(e.getUseCaseName(), index);
		tmp[index]= e;
	}
	@Override
	public ICode get(String useCaseName) {
		if (map.containsKey(useCaseName)) {
			return tmp[map.get(useCaseName)];
		} else {
			return null;
		}
	}
	@Override
	public void start() {
	}
	@Override
	public void reload() {
	}

}
