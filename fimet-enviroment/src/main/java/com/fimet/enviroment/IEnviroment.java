package com.fimet.enviroment;

import java.util.Map;

import com.fimet.socket.IConnectable;

public interface IEnviroment extends IConnectable {
	public String getName();
	public String getType();
	public Status getStatus();
	public Map<String, String> getProperties();
	public String getProperty(String name);
	public String getProperty(String name, String defaultValue);
	public Boolean getPropertyBoolean(String name);
	public Boolean getPropertyBoolean(String name, boolean defaultValue);
	public Integer getPropertyInteger(String name);
	public Integer getPropertyInteger(String name, int defaultValue);
	public Long getPropertyLong(String name);
	public Long getPropertyLong(String name, long defaultValue);
}
