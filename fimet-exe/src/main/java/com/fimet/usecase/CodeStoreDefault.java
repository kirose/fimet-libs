package com.fimet.usecase;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fimet.Manager;

@Component
public class CodeStoreDefault implements ICodeStore {
	private ECodeStore[] tmp;
	private Map<String,Integer> map;
	private int index;
	private static final int MAX_TMP_SIZE = Manager.getPropertyInteger("codestore.maxsize", 15);
	private String[] fields = Manager.getProperty("usecase.store.fields", "38,39").split(",");
	public CodeStoreDefault() {
		index = -1;
		tmp = new ECodeStore[MAX_TMP_SIZE];
		map = new HashMap<>();
		for (int i = 0; i < tmp.length; i++) {
			tmp[i] = new ECodeStore(fields.length);
		}
	}
	@Override
	public void store(IUseCase useCase) {
		index = (index+1)%MAX_TMP_SIZE;
		ECodeStore e = tmp[index];
		for (int i = 0; i < fields.length; i++) {
			e.setValue(i, useCase.getResponse().getValue(fields[i]));
		}
		map.put(useCase.getName(), index);
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
	@Override
	public void stop() {
	}

}
