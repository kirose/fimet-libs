package com.fimet.dao;

import java.util.List;

import com.fimet.IManager;
import com.fimet.enviroment.IEEnviroment;

public interface IEnviromentDAO extends IManager {
	List<IEEnviroment> findAll();
	IEEnviroment findByName(String name);
	IEEnviroment insert(IEEnviroment enviroment);
	IEEnviroment update(IEEnviroment enviroment);
	IEEnviroment delete(IEEnviroment enviroment);
}
