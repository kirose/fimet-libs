package com.fimet.preferences;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import com.fimet.FimetLogger;
import com.fimet.IPreferenceStore;
import com.fimet.IThreadManager;
import com.fimet.Manager;
import com.fimet.utils.FileUtils;

public class PreferenceStore implements IPreferenceStore {
	
	public static final int SAVE_CHANGES = 400;
	
	Queue<IPropertyChangeListener> queue = new ConcurrentLinkedQueue<IPropertyChangeListener>();
	Properties properties;
	AtomicInteger changes = new AtomicInteger(0);
	
	public PreferenceStore() {
		File file = new File("fimet", "fimet.properties");
		properties = new Properties();
	    InputStream stream = null;
		try {
			if (!file.exists()) {
				FimetLogger.warning(getClass(), "Cannot found properties (will be create):\n"+file.getAbsolutePath());
				file.createNewFile();
			}
			stream = new FileInputStream(file);
			properties.load(stream);
		} catch (Exception e) {
			FimetLogger.error(getClass(), "Preferences cannot found "+file.getAbsolutePath());
		} finally {
			FileUtils.close(stream);
		}
	}
	public void store() {
		Manager.get(IThreadManager.class).execute(()->{
			File file = new File("fimet", "fimet.properties");
		    OutputStream stream = null;
			try {
				stream = new FileOutputStream(file);
				properties.store(stream, "FIMET Properties");
			} catch (Exception e) {
				FimetLogger.error(getClass(), "Preferences Exception cannot found "+file.getAbsolutePath());
			} finally {
				FileUtils.close(stream);
			}
		});
	}
	
	@Override
	public void setValue(String name, Boolean value) {
		properties.setProperty(name, String.valueOf(value));
		fireChangeProperty(name);
	}
	
	@Override
	public Boolean getBoolean(String name) {
		String value = properties.getProperty(name);
		return value != null ? Boolean.valueOf(value) : null;
	}

	@Override
	public Boolean getBoolean(String name, boolean defaultValue) {
		String value = properties.getProperty(name);
		return value != null ? Boolean.valueOf(value) : defaultValue;
	}
	
	@Override
	public void setValue(String name, Integer value) {
		properties.setProperty(name, String.valueOf(value));
		fireChangeProperty(name);
	}
	
	@Override
	public Integer getInteger(String name) {
		String value = properties.getProperty(name);
		return value != null ? Integer.valueOf(value) : null;
	}

	@Override
	public Integer getInteger(String name, int defaultValue) {
		String value = properties.getProperty(name);
		return value != null ? Integer.valueOf(value) : defaultValue;
	}
	
	@Override
	public void setValue(String name, Long value) {
		properties.setProperty(name, String.valueOf(value));
		fireChangeProperty(name);
	}
	
	@Override
	public Long getLong(String name) {
		String value = properties.getProperty(name);
		return value != null ? Long.valueOf(value) : null;
	}

	@Override
	public Long getLong(String name, long defaultValue) {
		String value = properties.getProperty(name);
		return value != null ? Long.valueOf(value) : defaultValue;
	}

	
	@Override
	public void setValue(String name, String value) {
		properties.setProperty(name, value);
		fireChangeProperty(name);
	}
	
	@Override
	public String getString(String name) {
		return properties.getProperty(name);
	}

	@Override
	public String getString(String name, String defaultValue) {
		String value = properties.getProperty(name);
		return value != null ? value: defaultValue;
	}

	
	@Override
	public void setValue(String name, Double value) {
		properties.setProperty(name, String.valueOf(value));
		fireChangeProperty(name);
	}
	
	@Override
	public Double getDouble(String name) {
		String value = properties.getProperty(name);
		return value != null ? Double.valueOf(value) : null;
	}

	@Override
	public Double getDouble(String name, double defaultValue) {
		String value = properties.getProperty(name);
		return value != null ? Double.valueOf(value) : defaultValue;
	}

	@Override
	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		queue.add(listener);
	}

	@Override
	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		queue.remove(listener);
	}
	private void fireChangeProperty(String name) {
		changes.getAndIncrement();
		if (!queue.isEmpty()) {
			try {
				for (IPropertyChangeListener listener : queue) {
					listener.onPropertyChange(name);
				}
			} catch (Throwable e) {
				FimetLogger.error(getClass(), e);
			}
		}
		if (changes.get() == SAVE_CHANGES) {
			changes.set(0);
			store();
		}
	}
	@Override
	public void removePropery(String name) {
		properties.remove(name);
		fireChangeProperty(name);
	}
}
