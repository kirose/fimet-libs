package com.fimet.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import com.fimet.commons.FimetLogger;
import com.fimet.commons.exception.FimetException;
import com.fimet.core.xml.FimetXml;
import com.fimet.core.xml.ManagerXml;
import com.fimet.core.xml.PropertyXml;

/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public class Manager {
	private Map<Class<?>, Class<?>> managers = new HashMap<>();
	private Map<Class<?>, Object> instances = new HashMap<>();
	private Map<String,String> properties = new HashMap<>();
	private String source;
	private static Manager instance = new Manager();
	public static Manager getInstance() {
		return instance;
	}
	public Manager() {
		loadConfiguration();
	}
	void loadConfiguration() {
		loadDefaultManagers();
		try {
			//InputStream xmlFile = Manager.class.getClass().getResourceAsStream("resources/fimet.xml");
			java.io.File xmlFile = new java.io.File("resources/fimet.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(FimetXml.class);              
		    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		    FimetXml cfg = (FimetXml) jaxbUnmarshaller.unmarshal(xmlFile);
		    if (cfg.getManagers() != null && cfg.getManagers().getManagers() != null && !cfg.getManagers().getManagers().isEmpty()) {
		    	List<ManagerXml> managers = cfg.getManagers().getManagers();
		    	for (ManagerXml mgr : managers) {
		    		loadManager(mgr);
				}
		    }
		    loadProperties(cfg);
		    source = cfg.getSource() != null ? cfg.getSource().getPath() : null;
		} catch (Exception e) {
			Logger.getLogger(Manager.class).error("Loading fimet.xml",e);
		}
	}
	private void loadProperties(FimetXml cfg) {
		if (cfg != null && cfg.getProperties() != null && cfg.getProperties().getProperties()!= null) {
			for (PropertyXml p : cfg.getProperties().getProperties()) {
				properties.put(p.getName(), p.getValue());
			}
		}
	}
	private void loadDefaultManagers() {
		loadManager(com.fimet.core.IClassLoaderManager.class, "com.fimet.core.loader.ClassLoaderManager");
		loadManager(com.fimet.core.ICompilerManager.class, "com.fimet.core.compiler.CompilerManager");
		loadManager(com.fimet.core.IPreferencesManager.class, "com.fimet.core.preferences.PreferencesManager");
		loadManager(com.fimet.core.IExecutorManager.class, "com.fimet.core.usecase.exe.ExecutorManager");
		loadManager(com.fimet.core.IMessengerManager.class, "com.fimet.net.MessengerManager");
		loadManager(com.fimet.core.ISocketManager.class, "com.fimet.net.SocketManager");
		loadManager(com.fimet.core.ISimulatorManager.class, "com.fimet.simulator.SimulatorManager");
		loadManager(com.fimet.core.IFieldFormatManager.class, "com.fimet.parser.FieldFormatManager");
		loadManager(com.fimet.core.IFieldParserManager.class, "com.fimet.parser.FieldParserManager");
		loadManager(com.fimet.core.IParserManager.class, "com.fimet.parser.ParserManager");
	}
	private void loadManager(Class<?> extension, String className) {
		try {
			Class<?> clazz = Class.forName(className);			
			if (clazz == null) {
				FimetLogger.error(Manager.class,"Cannot found class "+className);
			} else if (!IManager.class.isAssignableFrom(extension)) {
				FimetLogger.error(Manager.class,"Interface "+extension.getName()+" is not assignable from "+IManager.class.getName());
			} else if (!extension.isAssignableFrom(clazz)) {
				FimetLogger.error(Manager.class,"Class "+clazz.getName()+" is not assignable from "+extension.getName());
			} else {
				managers.put(extension, clazz);
			}
		} catch (Throwable ex) {
			FimetLogger.error(Manager.class,"Error Manager load extensions");
		}
	}
	private void loadManager(ManagerXml mgr) {
		try {
			Class<?> extension = Class.forName(mgr.getId());
			Class<?> clazz = Class.forName(mgr.getClassName());
			if (extension == null) {
				FimetLogger.error(Manager.class,"Cannot found class extension "+mgr.getId());
			} else if (clazz == null) {
				FimetLogger.error(Manager.class,"Cannot found class "+mgr.getClassName());
			} else if (!IManager.class.isAssignableFrom(extension)) {
				FimetLogger.error(Manager.class,"Interface "+extension.getName()+" is not assignable from "+IManager.class.getName());
			} else if (!extension.isAssignableFrom(clazz)) {
				FimetLogger.error(Manager.class,"Class "+clazz.getName()+" is not assignable from "+extension.getName());
			} else {
				managers.put(extension, clazz);
			}
		} catch (Throwable ex) {
			FimetLogger.error(Manager.class,"Error Manager load extensions", ex);
		}
	}
	public static <I extends IManager,C> void manage(Class<I> i, Class<C> c) {
		getInstance().managers.put(i,c);
	}
	public static <I extends IManager,C> void manageIfNotExists(Class<I> i, Class<C> c) {
		if (!getInstance().managers.containsKey(i))
			getInstance().managers.put(i,c);
	}
	public static boolean isLoaded(Class<?> clazz) {
		return getInstance().instances.containsKey(clazz);
	}
	public static boolean isManaged(Class<?> clazz) {
		return getInstance().managers.containsKey(clazz);
	}
	public static <T> T get(Class<T> clazz) {
		if (getInstance().instances.containsKey(clazz)) {
			return clazz.cast(getInstance().instances.get(clazz));
		} else {
			if (!getInstance().managers.containsKey(clazz)) {
				//FimetLogger.error("Unregistred Manager "+clazz.getName());
				//System.err.println("Unregistred Manager "+clazz.getName());
				return null;
			} else {
				try {
					Object newInstance = getInstance().managers.get(clazz).newInstance();
					getInstance().instances.put(clazz, newInstance);
					if (newInstance instanceof IStartable) {
						((IStartable)newInstance).start();
					}
					return clazz.cast(newInstance);
				} catch (InstantiationException e) {
					throw new FimetException(e);
				} catch (IllegalAccessException e) {
					throw new FimetException(e);
				}
			}
		}
	}
	public static void saveAll() {
		for(Map.Entry<Class<?>, Object> e : getInstance().instances.entrySet()) {
			try {
				((IManager)e.getValue()).saveState();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	public static void freeAll() {
		for(Map.Entry<Class<?>, Object> e : getInstance().instances.entrySet()) {
			try {
				((IManager)e.getValue()).free();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	public static String getSource() {
		return getInstance().source;
	}
	public static String getProperty(String name) {
		return getInstance().properties.get(name);
	}
	public static Integer getPropertyInteger(String name) {
		String value = getProperty(name);
		if (value != null) {
			try {
				return Integer.valueOf(value);
			} catch (Exception e) {}
		}
		return null;
	}
	public static Long getPropertyLong(String name) {
		String value = getProperty(name);
		if (value != null) {
			try {
				return Long.valueOf(value);
			} catch (Exception e) {}
		}
		return null;
	}
}
