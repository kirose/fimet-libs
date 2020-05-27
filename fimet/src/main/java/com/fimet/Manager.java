package com.fimet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import com.fimet.xml.FimetXml;
import com.fimet.xml.ManagerXml;

/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public final class Manager {
	private Map<Class<?>, String> managers;
	private Map<Class<?>, Object> instances;
	private Map<String,String> properties;
	private Map<String,String> extensions;
	private Map<Class<?>, Object> singletons;
	private Set<Class<?>> autostart;
	private String source;
	public static final String FIMET = "FIMET";
	public static final String VERSION = "2.1.4"; 
	private static final Manager INSTANCE = new Manager();
	static {
		INSTANCE.start();
	}
	private Manager() {
		managers = new HashMap<>();
		instances = new HashMap<>();
		properties = new HashMap<>();
		extensions = new HashMap<>();
		singletons = new HashMap<>();
		autostart = new HashSet<>();
		loadConfiguration();
	}
	private void start() {
		if (!autostart.isEmpty()) {
			for (Class<?> clazz : autostart) {
				_get(clazz);
			}
		}
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
			FimetLogger.error(Manager.class,"Loading fimet.xml",e);
			throw new FimetException("Cannot found fimet.xml file",e);
		}
	}
	void loadConfiguration() {
		loadDefaultManagers();
		loadDefaultAutostarts();
		try {
			FimetXml cfg = getModel();
			properties = cfg.getPropertiesMap();
			extensions = cfg.getExtensionsMap();
		    if (cfg.getManagers() != null && !cfg.getManagers().isEmpty()) {
		    	List<ManagerXml> managers = cfg.getManagers();
		    	for (ManagerXml mgr : managers) {
		    		Class<?> iManager = findIManagerClass(mgr.getId());
		    		this.managers.put(iManager, mgr.getClassName());
		    		if(mgr.isAutostart()) {
		    			autostart.add(iManager);
		    		}
				}
		    }
		} catch (Exception e) {
			FimetLogger.error(Manager.class,"Loading fimet.xml",e);
			throw new FimetException("Cannot found fimet.xml file",e);
		}
	}
	private void loadDefaultAutostarts() {
		autostart.add(IAdapterManager.class);
		autostart.add(IParserManager.class);
		autostart.add(IFieldGroupManager.class);
	}
	private void loadDefaultManagers() {
		managers.put(ITimerManager.class, "com.fimet.utils.TimerManager");
		managers.put(IClassLoaderManager.class, "com.fimet.utils.ClassLoaderManager");
		managers.put(ICompilerManager.class, "com.fimet.utils.CompilerManager");
		managers.put(IThreadManager.class, "com.fimet.utils.ThreadManager");
		managers.put(ISQLiteManager.class, "com.fimet.sqlite.SQLiteManager");
		managers.put(IPreferencesManager.class, "com.fimet.preferences.PreferencesManager");
		managers.put(IAdapterManager.class, "com.fimet.parser.adapter.AdapterManager");
		managers.put(IFieldGroupManager.class, "com.fimet.parser.FieldGroupManager");
		managers.put(IParserManager.class, "com.fimet.parser.ParserManager");
	}
	private Class<?> findIManagerClass(String className) {
		try {
			Class<?> extension = Class.forName(className);			
			if (extension == null) {
				throw new FimetException("Cannot found class "+className);
			} else if (!IManager.class.isAssignableFrom(extension)) {
				throw new FimetException("Interface "+extension.getName()+" is not assignable from "+IManager.class.getName());
			} else {
				return extension;
			}
		} catch (Throwable ex) {
			FimetLogger.error(Manager.class,"Error Manager load extensions",ex);
			throw new FimetException("Error Manager load extensions",ex);
		}
	}
	private Class<?> findManagerClass(Class<?> extension, String className) {
		try {
			Class<?> clazz = Class.forName(className);			
			if (clazz == null) {
				throw new FimetException("Cannot found class "+className);
			} else if (!IManager.class.isAssignableFrom(extension)) {
				throw new FimetException("Interface "+extension.getName()+" is not assignable from "+IManager.class.getName());
			} else if (!extension.isAssignableFrom(clazz)) {
				throw new FimetException("Class "+clazz.getName()+" is not assignable from "+extension.getName());
			} else {
				return clazz;
			}
		} catch (Throwable ex) {
			FimetLogger.error(Manager.class,"Error Manager load extensions",ex);
			throw new FimetException("Error Manager load extensions",ex);
		}
	}
	public static boolean isLoaded(Class<?> clazz) {
		return INSTANCE.instances.containsKey(clazz);
	}
	public static boolean isManaged(Class<?> iManagerClass) {
		return INSTANCE.managers.containsKey(iManagerClass);
	}
	private <T> T _get(Class<T> iManagerClass) {
		if (instances.containsKey(iManagerClass)) {
			return iManagerClass.cast(instances.get(iManagerClass));
		} else {
			String managerClassName = managers.get(iManagerClass);
			if (managerClassName == null) {
				FimetLogger.error(Manager.class,"Class "+iManagerClass.getName()+" is not managed, you must declare it as a manager in fimet.xml");
				throw new FimetException("Class "+iManagerClass.getName()+" is not managed, you must declare it as a manager in fimet.xml");
			}
			Class<?> classManager = findManagerClass(iManagerClass, managerClassName);
			IManager newManagerInstance = null;
			try {
				newManagerInstance = (IManager)classManager.newInstance();
			} catch (Exception e) {
				FimetLogger.error(Manager.class,"Manager Instantiation Exception for "+classManager.getName(),e);
				throw new FimetException("Manager Instantiation Exception for "+classManager.getName(), e);
			}
			instances.put(iManagerClass, newManagerInstance);
			newManagerInstance.start();
			return iManagerClass.cast(newManagerInstance);
		}
	}
	@SuppressWarnings("unchecked")
	public static <T extends IManager> T get(String className) {
		try {
			Class<?> clazz = Class.forName(className);
			return INSTANCE._get((Class<T>)clazz);
		} catch (ClassNotFoundException e) {
			FimetLogger.error(Manager.class,"Invalid manager class "+className,e);
			throw new FimetException("Invalid manager class "+className, e);
		}
	}
	public static <T extends IManager> T get(Class<T> clazz) {
		return INSTANCE._get(clazz);
	}
	public static void saveAll() {
		for(Map.Entry<Class<?>, Object> e : INSTANCE.instances.entrySet()) {
			try {
				((IManager)e.getValue()).saveState();
			} catch (Exception ex) {
				FimetLogger.error(Manager.class,"Manager save state exception ",ex);
			}
		}
	}
	public static void reloadAll() {
		for(Map.Entry<Class<?>, Object> e : INSTANCE.instances.entrySet()) {
			try {
				IManager manager = (IManager)e.getValue();
				manager.saveState();
				manager.reload();
			} catch (Exception ex) {
				FimetLogger.error(Manager.class,"Manager reload exception ",ex);
			}
		}		
	}
	public static String getSource() {
		return INSTANCE.source;
	}
	public static String getProperty(String name) {
		return INSTANCE.properties.get(name);
	}
	public static String getProperty(String name, String defaultValue) {
		String value = getProperty(name);
		if (value == null) {
			return defaultValue;
		}
		return value;
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
	@SuppressWarnings("unchecked")
	private <T> T _newExtension(Class<T> iClazz) {
		String extensionClassName = extensions.get(iClazz.getName());
		if (extensionClassName != null) {
			try {
				Class<?> clazz = Class.forName(extensionClassName);
				if (iClazz.isAssignableFrom(clazz)) {
					return (T) clazz.newInstance();
				} else {
					FimetLogger.error(Manager.class,"Extension error, "+extensionClassName+" must implements "+iClazz.getName());
					throw new FimetException("Extension error, "+extensionClassName+" must implements "+iClazz.getName());
				}
			} catch (Exception e) {
				FimetLogger.error(Manager.class,"Invalid extension "+extensionClassName,e);
				throw new FimetException("Invalid extension "+extensionClassName,e);
			}
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	private <T> T _getExtension(Class<T> iClazz) {
		if (instances.containsKey(iClazz)) {
			return (T)instances.get(iClazz);
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	private <E extends T,T> T _getExtension(Class<T> iClazz, E defaultInstance) {
		if (instances.containsKey(iClazz)) {
			return (T)instances.get(iClazz);
		} else {
			T instance = _newExtension(iClazz);
			if (instance != null) {
				INSTANCE.instances.put(iClazz, instance);
				return instance;
			} else if (defaultInstance != null) {
				INSTANCE.instances.put(iClazz, defaultInstance);
				return defaultInstance;
			} else {
				throw new NullPointerException();
			}
		}
	}
	@SuppressWarnings("unchecked")
	private <E extends T,T> T _getExtension(Class<T> iClazz, Class<E> defaultClass) {
		if (instances.containsKey(iClazz)) {
			return (T)instances.get(iClazz);
		} else {
			T instance = _newExtension(iClazz);
			if (instance != null) {
				INSTANCE.instances.put(iClazz, instance);
				return instance;
			} else if (defaultClass != null) {
				E einstance;
				try {
					einstance = defaultClass.newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					throw new FimetException(e); 
				}
				INSTANCE.instances.put(iClazz, einstance);
				return einstance;
			} else {
				throw new NullPointerException();
			}
		}
	}
	public static <E extends T,T> T getExtension(Class<T> iClazz) {
		return INSTANCE._getExtension(iClazz);
	}
	public static <E extends T,T> T getExtension(Class<T> iClazz, E defaultInstance) {
		return INSTANCE._getExtension(iClazz, defaultInstance);
	}
	public static <E extends T,T> T getExtension(Class<T> iClazz, Class<E> defaultClass){
		return INSTANCE._getExtension(iClazz, defaultClass);
	}
	@SuppressWarnings("unchecked")
	private <T> T _getSingleton(Class<T> clazz) {
		if (singletons.containsKey(clazz)) {
			return (T)singletons.get(clazz);
		} else {
			try {
				T instance = clazz.newInstance();
				singletons.put(clazz, instance);
				return instance;
			} catch (Throwable t) {
				FimetLogger.error(Manager.class,"Error singleton instantiation, "+clazz.getName(),t);
				throw new FimetException("Error singleton instantiation, "+clazz.getName(), t);
			}
		}
	}
	public static <T> T getSingleton(Class<T> clazz){
		return INSTANCE._getSingleton(clazz);
	}
}
