package com.fimet.dao;

import java.util.List;

import com.fimet.IManager;
import com.fimet.enviroment.IEEnviroment;

public interface IEnviromentDAO<T extends IEEnviroment> extends IManager {
	List<T> findAll();
	T findByName(String name);
	T insert(T enviroment);
	T update(T enviroment);
	T delete(T enviroment);
}

