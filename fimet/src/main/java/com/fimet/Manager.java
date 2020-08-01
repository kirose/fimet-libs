package com.fimet;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.fimet.preferences.PreferenceStore;
import com.fimet.utils.ArrayUtils;
import com.fimet.utils.ManagerUtils;
import com.fimet.utils.XmlUtils;
import com.fimet.xml.ExtensionXml;
import com.fimet.xml.FimetXml;
import com.fimet.xml.ManagerXml;

/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public final class Manager implements IManagerVisitor {
	private Map<Class<?>, Object> managers = new HashMap<>();
	private Map<String, String> managersString = new LinkedHashMap<>();
	private Map<Class<?>, Object> instances = new HashMap<>();
	private Map<Class<?>,List<?>> extensions = new HashMap<>();
	private Map<String,List<String>> extensionsString = new LinkedHashMap<>();
	private Map<Class<?>, Object> singletons = new HashMap<>();
	private Set<String> autostart = new HashSet<>();
	private String source;
	private IPreferenceStore store;
	public static final String FIMET = "FIMET";
	public static final String VERSION = "2.2.0"; 
	private static final Manager INSTANCE = new Manager();
	private IClassLoaderManager loader; 
	static {
		INSTANCE.init();
	}
	private Manager() {
		store = new PreferenceStore();
		loadConfiguration();
	}
	@SuppressWarnings("unchecked")
	private void init() {
		loader = new ClassLoaderManager();
		instances.put(IClassLoaderManager.class, loader);
		if (!extensionsString.isEmpty()) {
			for (Entry<String, List<String>> e : extensionsString.entrySet()) {
				Class<? extends IExtension> iExtensionClass = (Class<? extends IExtension>)findIExtensionClass(e.getKey());
				extensions.put(iExtensionClass, e.getValue());
			}
		}
		if (!managersString.isEmpty()) {
			for (Entry<String, String> e : managersString.entrySet()) {
				Class<? extends IManager> iManagerClass = (Class<? extends IManager>)findIManagerClass(e.getKey());
				ManagerUtils.acceptManager(iManagerClass, e.getValue(), this);
			}
		}
		if (!autostart.isEmpty()) {
			for (String clazz : autostart) {
				Class<? extends IManager> iManagerClass = findIManagerClass(clazz);
				_get(iManagerClass);
			}
		}
		extensionsString = null;
		managersString = null;
		autostart = null;
	}
	@Override
	public <T extends IManager> void visitManager(Class<T> iManagerClass, Object manager) {
		managers.put(iManagerClass, manager);
	}
	public static FimetXml getModel() {
		FimetXml fimetXml = XmlUtils.fromFile(new File("fimet.xml"), FimetXml.class);
		return fimetXml;
	}
	private void loadConfiguration() {
		loadDefaultManagers();
		loadDefaultAutostarts();
		loadDefaultProperties();
		try {
			FimetXml cfg = getModel();
			Map<String, String> prop = cfg.getPropertiesMap();
			if (prop!=null&&!prop.isEmpty()) {
				for (Entry<String, String> e : prop.entrySet()) {
					store.setValue(e.getKey(), e.getValue());
				}
			}
			List<ExtensionXml> ext = cfg.getExtensions();
			if (ext!=null&&!ext.isEmpty()) {
				for (ExtensionXml e : ext) {
					if (extensionsString.containsKey(e.getName())) {
						extensionsString.get(e.getName()).add(e.getClassName());
					} else {
						List<String> list = new ArrayList<>();
						list.add(e.getClassName());
						extensionsString.put(e.getName(), list);
					}
				}
			}
			if (cfg.getManagers() != null && !cfg.getManagers().isEmpty()) {
				List<ManagerXml> managers = cfg.getManagers();
				for (ManagerXml mgr : managers) {
					this.managersString.put(mgr.getName(), mgr.getClassName());
					if(mgr.isAutostart()) {
						autostart.add(mgr.getName());
					}
				}
			}
		} catch (Exception e) {
			FimetLogger.error(Manager.class,"Loading fimet.xml",e);
			throw new FimetException("Cannot found fimet.xml file",e);
		}
	}
	private void loadDefaultProperties() {
		store.setValue("classpath.bin", "fimet/bin");
		store.setValue("classpath.lib", "fimet/lib");
		store.setValue("classpath.src", "fimet/src");
	}
	private void loadDefaultAutostarts() {
	}
	private void loadDefaultManagers() {
		//managersString.put("com.fimet.IEventManager", "com.fimet.EventManager");
		//managersString.put("com.fimet.ITimerManager", "com.fimet.TimerManager");
		//managersString.put("com.fimet.IClassLoaderManager", "com.fimet.ClassLoaderManager");
		//managersString.put("com.fimet.ICompilerManager", "com.fimet.CompilerManager");
		//managersString.put("com.fimet.IThreadManager", "com.fimet.ThreadManager");
	}
	@SuppressWarnings("unchecked")
	private Class<? extends IManager> findIManagerClass(String className) {
		try {
			Class<?> extension = loader.forName(className);			
			if (extension == null) {
				throw new FimetException("Cannot found class "+className);
			} else if (!IManager.class.isAssignableFrom(extension)) {
				throw new FimetException("Interface "+extension.getName()+" is not assignable from "+IManager.class.getName());
			} else {
				return (Class<? extends IManager>)extension;
			}
		} catch (Throwable ex) {
			FimetLogger.error(Manager.class,"Error Manager load extensions",ex);
			throw new FimetException("Error Manager load extensions",ex);
		}
	}
	private Class<?> findManagerClass(Class<?> extension, String className) {
		try {
			Class<?> clazz = loader.forName(className);
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
			FimetLogger.error(Manager.class,"Error Load Manager",ex);
			throw new FimetException("Error Load Manager",ex);
		}
	}
	private Class<?> findExtensionClass(Class<?> extension, String className) {
		try {
			Class<?> clazz = loader.forName(className);
			if (clazz == null) {
				throw new FimetException("Cannot found class "+className);
			} else if (!IExtension.class.isAssignableFrom(extension)) {
				throw new FimetException("Interface "+extension.getName()+" is not assignable from "+IManager.class.getName());
			} else if (!extension.isAssignableFrom(clazz)) {
				throw new FimetException("Class "+clazz.getName()+" is not assignable from "+extension.getName());
			} else {
				return clazz;
			}
		} catch (Throwable ex) {
			FimetLogger.error(Manager.class,"Error Load Extension",ex);
			throw new FimetException("Error Load Extension",ex);
		}
	}
	private Class<?> findIExtensionClass(String className) {
		try {
			Class<?> extension = loader.forName(className);			
			if (extension == null) {
				throw new FimetException("Cannot found class "+className);
			} else if (!IExtension.class.isAssignableFrom(extension)) {
				throw new FimetException("Interface "+extension.getName()+" is not assignable from "+IExtension.class.getName());
			} else {
				return extension;
			}
		} catch (Throwable ex) {
			FimetLogger.error(Manager.class,"Extension Error",ex);
			throw new FimetException("Extension Error",ex);
		}
	}
	public static boolean isLoaded(Class<?> clazz) {
		return INSTANCE.instances.containsKey(clazz);
	}
	public static boolean isManaged(Class<?> iManagerClass) {
		return INSTANCE.managers.containsKey(iManagerClass);
	}
	private <E extends T,T extends IManager> T _get(Class<T> iManagerClass) {
		return _get(iManagerClass, (Class<? extends T>)null);
	}
	@SuppressWarnings("unchecked")
	private <E extends T,T extends IManager> T _get(Class<T> iManagerClass, Class<E> defaultIClass) {
		if (instances.containsKey(iManagerClass)) {
			return iManagerClass.cast(instances.get(iManagerClass));
		} else {
			Object managerDeclaration = managers.get(iManagerClass);
			if (managerDeclaration == null) {
				if (defaultIClass != null) {
					try {
						T instance = defaultIClass.newInstance();
						instances.put(iManagerClass, instance);
						return instance;
					} catch (Exception e) {
						FimetLogger.error(Manager.class,"Invalid manager "+defaultIClass,e);
						if (e instanceof FimetException) {
							throw (FimetException)e;
						}
						throw new FimetException("Invalid manager "+defaultIClass,e);
					}
				} else {
					FimetLogger.error(Manager.class,"Class "+iManagerClass.getName()+" is not managed, you must declare it as a manager in fimet.xml");
					throw new FimetException("Class "+iManagerClass.getName()+" is not managed, you must declare it as a manager in fimet.xml");
				}
			} else if (managerDeclaration instanceof String) {
				String managerClassName = (String)managerDeclaration;
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
			} else if (managerDeclaration instanceof Class) {
				T instance = _get((Class<T>)managerDeclaration, defaultIClass);
				instances.put(iManagerClass, instance);
				return instance;
			} else {
				FimetLogger.error(Manager.class,"Class "+iManagerClass.getName()+" invalid source declaration "+managerDeclaration.getClass());
				throw new FimetException("Class "+iManagerClass.getName()+" invalid source declaration "+managerDeclaration.getClass());
			}
		}
	}
	@SuppressWarnings("unchecked")
	private <E extends T, T extends IManager> T _get(Class<T> iClazz, E defaultInstance) {
		if (instances.containsKey(iClazz)) {
			return (E)instances.get(iClazz);
		} else if (managers.containsKey(iClazz)) {
			return _get(iClazz, (Class<? extends T>)null);
		} else if (defaultInstance != null) {
			instances.put(iClazz, defaultInstance);
			return defaultInstance;
		} else {
			throw new ExtensionException("Not declared manager "+iClazz.getName()+", you must declare it as a manager in fimet.xml");			
		}
	}
	@SuppressWarnings("unchecked")
	public static <T extends IManager> T get(String className) {
		try {
			Class<?> clazz = INSTANCE.loader.forName(className);
			return INSTANCE._get((Class<T>)clazz, (Class<? extends T>)null);
		} catch (Exception e) {
			FimetLogger.error(Manager.class,"Invalid manager class "+className,e);
			throw new FimetException("Invalid manager class "+className, e);
		}
	}
	public static <T extends IManager> T get(Class<T> clazz) {
		return INSTANCE._get(clazz, (Class<? extends T>)null);
	}
	public static <E extends T,T extends IManager> T get(Class<T> clazz, Class<E> defaultClass) {
		return INSTANCE._get(clazz, defaultClass);
	}
	public static <E extends T,T extends IManager> T get(Class<T> clazz, E defaultInstance) {
		return INSTANCE._get(clazz, defaultInstance);
	}
	public static void reloadAll() {
		for(Map.Entry<Class<?>, Object> e : INSTANCE.instances.entrySet()) {
			try {
				IManager manager = (IManager)e.getValue();
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
		return INSTANCE.store.getString(name);
	}
	public static String getProperty(String name, String defaultValue) {
		return INSTANCE.store.getString(name, defaultValue);
	}
	public static Integer getPropertyInteger(String name) {
		return INSTANCE.store.getInteger(name);
	}
	public static Integer getPropertyInteger(String name, Integer defaultValue) {
		return INSTANCE.store.getInteger(name, defaultValue);
	}
	public static Long getPropertyLong(String name) {
		return INSTANCE.store.getLong(name);
	}
	public static Long getPropertyLong(String name, Long defaultValue) {
		return INSTANCE.store.getLong(name, defaultValue);
	}
	public static Boolean getPropertyBoolean(String name) {
		return INSTANCE.store.getBoolean(name);
	}
	public static Boolean getPropertyBoolean(String name, Boolean defaultValue) {
		return INSTANCE.store.getBoolean(name, defaultValue);
	}
	@SuppressWarnings("unchecked")
	private <E extends T, T extends IExtension> List<T> _getExtensions(Class<T> iClazz) {
		if (extensions.containsKey(iClazz)) {
			List<?> list = extensions.get(iClazz);
			if (list != null && !list.isEmpty()) {
				if (list.get(0) instanceof String) {
					List<T> instances = new ArrayList<>(list.size());
					for (Object object : list) {
						String className = (String)object;
						Class<?> extensionClass = findExtensionClass(iClazz, className);
						T instance;
						try {
							instance = (T)extensionClass.newInstance();
							instances.add(instance);
						} catch (Exception e) {
							throw new FimetException("Instantiation exception for "+extensionClass.getName(),e);
						}
					}
					extensions.put(iClazz, instances);	
					return ArrayUtils.copy(instances);
				} else {
					List<T> instances = new ArrayList<>(list.size());
					for (Object object : list) {
						instances.add((T)object);
					}
					return instances; 
				}
			}
			return null;
		} else {
			return null;			
		}
	}
	public static <M extends I,I> void setManager(Class<I> iClazz, Class<M> mClass) {
		INSTANCE._setManager(iClazz, mClass);
	}
	public <M extends I,I> void _setManager(Class<I> iClazz, Class<M> mClass) {
		try {
			if (instances.containsKey(iClazz)) {
				M m = mClass.newInstance();
				FimetLogger.debug(Manager.class,"Manager "+instances.get(iClazz)+" will be override by"+m);
				instances.put(iClazz, m);
			}
			managers.put(iClazz, mClass.getName());
		} catch(Exception e) {
			FimetLogger.error(Manager.class,"Cannot set "+mClass+" as manager");
			throw new FimetException("Cannot set "+mClass+" as manager");
		}
	}
	public static <E extends T,T extends IExtension> List<T> getExtensions(Class<T> iClazz) {
		return INSTANCE._getExtensions(iClazz);
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
	public static IPreferenceStore getPreferenceStore() {
		return INSTANCE.store;
	}
}
