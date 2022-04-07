package com.fimet.dao;

import java.util.List;

import com.fimet.IManager;
import com.fimet.net.IESocket;

public interface ISocketDAO<T extends IESocket> extends IManager {
	T findByName(String name);
	List<T> findAll();
	T insert(T entity);
	T update(T entity);
	T delete(T entity);
}
