package com.fimet;

import com.fimet.preferences.IPropertyChangeListener;

public interface IPreferenceStore {
	void store();
	void removePropery(String name);
	void setValue(String name, Boolean value);
	Boolean getBoolean(String name);
	Boolean getBoolean(String name, boolean defaultValue);
	void setValue(String name, Integer value);
	Integer getInteger(String name);
	Integer getInteger(String name, int defaultValue);
	void setValue(String name, Long value);
	Long getLong(String name);
	Long getLong(String name, long defaultValue);
	void setValue(String name, String value);
	String getString(String name);
	String getString(String name, String defaultValue);
	void setValue(String name, Double value);
	Double getDouble(String name);
	Double getDouble(String name, double defaultValue);
	void addPropertyChangeListener(IPropertyChangeListener listener);
	void removePropertyChangeListener(IPropertyChangeListener listener);
}
