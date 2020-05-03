package com.fimet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import com.fimet.commons.FimetLogger;
import com.fimet.commons.exception.FimetException;
import com.fimet.xml.ExtensionXml;
import com.fimet.xml.FimetXml;
import com.fimet.xml.ManagerXml;
import com.fimet.xml.PropertyXml;

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
	public static FimetXml getModel() {
		try {
			//InputStream xmlFile = Manager.class.getClass().getResourceAsStream("resources/fimet.xml");
			java.io.File xmlFile = new java.io.File("resources/fimet.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(FimetXml.class);              
		    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		    FimetXml cfg = (FimetXml) jaxbUnmarshaller.unmarshal(xmlFile);
		    return cfg;
		} catch (Exception e) {
			Logger.getLogger(Manager.class).error("Loading fimet.xml",e);
			throw new FimetException("Cannot found fimet.xml file");
		}
	}
	void loadConfiguration() {
		try {
			FimetXml cfg = getModel();
			loadProperties(cfg);
			loadDefaultManagers();
		    if (cfg.getManagers() != null && cfg.getManagers().getManagers() != null && !cfg.getManagers().getManagers().isEmpty()) {
		    	List<ManagerXml> managers = cfg.getManagers().getManagers();
		    	for (ManagerXml mgr : managers) {
		    		loadManager(mgr);
				}
		    }
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
		loadManager(ITimerManager.class, "com.fimet.timer.TimerManager");
		loadManager(IClassLoaderManager.class, "com.fimet.loader.ClassLoaderManager");
		loadManager(ICompilerManager.class, "com.fimet.compiler.CompilerManager");
		loadManager(IPreferencesManager.class, "com.fimet.preferences.PreferencesManager");
		loadManager(ISessionManager.class, "com.fimet.usecase.SessionManager");
		loadManager(IUseCaseManager.class, "com.fimet.usecase.UseCaseManager");
		loadManager(IAdapterManager.class, "com.fimet.adapter.AdapterManager");
		loadManager(ISocketManager.class, "com.fimet.net.SocketManager");
		loadManager(ISimulatorModelManager.class, "com.fimet.simulator.SimulatorModelManager");
		loadManager(ISimulatorManager.class, "com.fimet.simulator.SimulatorManager");
		loadManager(IFieldFormatManager.class, "com.fimet.parser.FieldFormatManager");
		loadManager(IFieldParserManager.class, "com.fimet.parser.FieldParserManager");
		loadManager(IParserManager.class, "com.fimet.parser.ParserManager");
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
			//FimetLogger.error(Manager.class,"Error Manager load extensions",ex);
			throw new FimetException("Error Manager load extensions",ex);
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
			if (mgr.getExtensions() != null && !mgr.getExtensions().isEmpty()) {
				for (ExtensionXml e : mgr.getExtensions()) {
					properties.put(e.getId(), e.getClassName());
				}
			}
		} catch (Throwable ex) {
			//FimetLogger.error(Manager.class,"Error Manager load extensions", ex);
			throw new FimetException("Error Manager load extensions",ex);
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
	public static Integer getPropertyInteger(String name, Integer defaultValue) {
		Integer value = getPropertyInteger(name);
		if (value == null) {
			return defaultValue;
		}
		return value;
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
	public static Long getPropertyLong(String name, Long defaultValue) {
		Long value = getPropertyLong(name);
		if (value == null) {
			return defaultValue;
		}
		return value;
	}
	public static Boolean getPropertyBoolean(String name) {
		String value = getProperty(name);
		if (value != null) {
			try {
				return Boolean.valueOf(value);
			} catch (Exception e) {}
		}
		return null;
	}
	public static Boolean getPropertyBoolean(String name, Boolean defaultValue) {
		Boolean value = getPropertyBoolean(name);
		if (value == null) {
			return defaultValue;
		}
		return value;
	}
	public static <T> T newInstanceForExtension(Class<T> iClazz){
		return newInstanceForExtension(iClazz, null);
	}
	@SuppressWarnings("unchecked")
	public static <E extends T,T> T newInstanceForExtension(Class<T> iClazz, Class<E> defaultClass){
		String extensionClassName = getProperty(iClazz.getName());
		if (extensionClassName != null) {
			try {
				Class<?> clazz = Class.forName(extensionClassName);
				if (iClazz.isAssignableFrom(clazz)) {
					return (T) clazz.newInstance();
				} else {
					throw new FimetException("Extension error, "+extensionClassName+" must implements "+iClazz.getName());
				}
			} catch (Exception e) {
				throw new FimetException("Invalid extension "+extensionClassName,e);
			}
		} else if (defaultClass != null) {
			try {
				return defaultClass.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				throw new FimetException(e); 
			}
		} else {
			return null;
		}
	}
}
