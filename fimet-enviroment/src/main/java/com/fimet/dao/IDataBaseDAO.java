package com.fimet.dao;

import java.util.List;

import com.fimet.IManager;
import com.fimet.database.IEDataBase;

public interface IDataBaseDAO extends IManager {
	<T extends IEDataBase> T findByName(String name);
	List<? extends IEDataBase> findAll();
}
