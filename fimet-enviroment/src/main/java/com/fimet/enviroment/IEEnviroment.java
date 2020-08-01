package com.fimet.enviroment;

import java.util.Map;

public interface IEEnviroment {
	public String getName();
	public void setName(String name);
	public String getType();
	public void setType(String type);
	public Map<String, String> getProperties();
	public void setProperties(Map<String, String> properties);
	public void setProperty(String name, String value);
	public String getProperty(String name);
	public String getProperty(String name, String defaultValue);
	public Boolean getPropertyBoolean(String name);
	public Boolean getPropertyBoolean(String name, boolean defaultValue);
	public Integer getPropertyInt(String name);
	public Integer getPropertyInt(String name, int defaultValue);
}
