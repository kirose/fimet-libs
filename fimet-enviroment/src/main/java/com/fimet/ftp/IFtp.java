package com.fimet.ftp;

import java.io.File;

import com.fimet.socket.IConnectable;

public interface IFtp extends IConnectable {
	public String getName();
	public Integer getPort();
	public String getAddress();
	public String getUser();
	public String execute(String command) throws FtpException;
	public long download(String command, File out) throws FtpException;
	public void download(String command, File out, IFtpDownloadListener listener) throws FtpException;
}
