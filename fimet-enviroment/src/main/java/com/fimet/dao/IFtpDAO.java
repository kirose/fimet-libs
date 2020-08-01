package com.fimet.dao;

import java.util.List;

import com.fimet.IManager;
import com.fimet.ftp.IEFtp;

public interface IFtpDAO extends IManager {
	List<IEFtp> findAll();
	IEFtp findByName(String name);
	IEFtp insert(IEFtp ftp);
	IEFtp update(IEFtp ftp);
	IEFtp delete(IEFtp ftp);
}
