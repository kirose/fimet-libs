package com.fimet.usecase;


import com.fimet.ISQLiteManager;
import com.fimet.Manager;
import com.fimet.dao.CodeStoreDAO;
import com.fimet.entity.ECodeStore;
import com.j256.ormlite.support.ConnectionSource;

public class CodeStore implements ICodeStore {
	private ECodeStore[] tmp;
	private int index;
	private static final int MAX_TMP_SIZE = Manager.getPropertyInteger("store.codeMaxSizeRuntime", 10);
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
		tmp[index]= e;
		dao.insertOrUpdateAsync(e);
	}
	@Override
	public ECodeStore get(String useCaseName) {
		int index;
		if ((index = indexOf(useCaseName)) != -1) {
			return tmp[index];
		} else {
			return dao.findById(useCaseName);
		}
	}
	private int indexOf(String name) {
		for (int i = 0; i < tmp.length; i++) {
			if (tmp[i].getUseCaseName().equals(name)) {
				return i;
			}
		}
		return -1;
	}  
}
