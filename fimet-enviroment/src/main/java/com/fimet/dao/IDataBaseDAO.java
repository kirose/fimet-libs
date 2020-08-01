package com.fimet.dao;

import java.util.List;

import com.fimet.IManager;
import com.fimet.database.IEDataBase;

public interface IDataBaseDAO extends IManager {
	IEDataBase findByName(String name);
	List<IEDataBase> findAll();
}
