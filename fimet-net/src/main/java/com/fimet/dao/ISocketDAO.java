package com.fimet.dao;

import java.util.List;

import com.fimet.IManager;
import com.fimet.socket.IESocket;

public interface ISocketDAO extends IManager {
	IESocket findByName(String name);
	List<IESocket> findAll();
	IESocket insert(IESocket entity);
	IESocket update(IESocket entity);
	IESocket delete(IESocket entity);
}
