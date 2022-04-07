package com.fimet.net;

public interface IESocket {
	public String getName();
	public String getAddress();
	public Integer getPort();
	public Boolean isServer();
	public String getAdapter();
	public Integer getMaxConnections();
}
