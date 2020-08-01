package com.fimet;

import java.util.List;

import com.fimet.ftp.IEFtp;
import com.fimet.ftp.IFtp;

public interface IFtpManager extends IManager {
	public IFtp connect(String name);
	public IFtp connect(IEFtp entity);
	public IFtp disconnect(String name);
	public IFtp disconnect(IFtp connection);
	public void remove(IFtp ftp);
	public IFtp getFtp(String name);
	public IFtp getFtp(IEFtp entity);
	public List<IFtp> getFtps();
}
