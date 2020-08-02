package com.fimet.dao;

import java.util.List;

import com.fimet.IManager;
import com.fimet.ftp.IEFtp;

public interface IFtpDAO<T extends IEFtp> extends IManager {
	List<T> findAll();
	T findByName(String name);
	T insert(T ftp);
	T update(T ftp);
	T delete(T ftp);
}
