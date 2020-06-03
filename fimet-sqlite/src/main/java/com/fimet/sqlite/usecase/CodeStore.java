package com.fimet.sqlite.usecase;


import java.util.HashMap;
import java.util.Map;

import com.fimet.ISQLiteManager;
import com.fimet.Manager;
import com.fimet.sqlite.dao.CodeStoreDAO;
import com.fimet.sqlite.entity.ECodeStore;
import com.fimet.usecase.ICode;
import com.fimet.usecase.ICodeStore;
import com.j256.ormlite.support.ConnectionSource;

public class CodeStore implements ICodeStore {
	private ECodeStore[] tmp;
	private int index;
	private Map<String,Integer> map = new HashMap<>();
	private static final int MAX_TMP_SIZE = Manager.getPropertyInteger("codestore.maxSize", 15);
	private CodeStoreDAO dao;
	public CodeStore() {
		tmp = new ECodeStore[MAX_TMP_SIZE];
		index = -1;
		ConnectionSource connection = Manager.get(ISQLiteManager.class).getConnection("store-codes");
		dao = new CodeStoreDAO(connection);
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
		dao.insertOrUpdateAsync(e);
	}
	@Override
	public ICode get(String useCaseName) {
		if (map.containsKey(useCaseName)) {
			return tmp[map.get(useCaseName)];
		} else {
			return dao.findById(useCaseName);
		}
	}
}
