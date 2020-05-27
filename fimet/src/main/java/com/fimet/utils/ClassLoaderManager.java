package com.fimet.utils;

import com.fimet.AbstractManager;
import com.fimet.IClassLoaderManager;


/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public class ClassLoaderManager extends AbstractManager implements IClassLoaderManager {
	
	private ClassLoaderBin classLoaderBin = new ClassLoaderBin();
	private ClassLoaderLib classLoaderLib = new ClassLoaderLib();
	@Override
	public Class<?> loadClass(String className) throws ClassLoaderException{
		if (wasInstalled(className)) {
			try {
				return loadClassBin(className);
			} catch (Exception e) {
				try {
					return Class.forName(className);
				} catch (Exception t) {
					throw new ClassLoaderException(t);
				}
			}
		} else {
			try {
				return loadClassLib(className);
			} catch (Exception e) {
				try {
					return Class.forName(className);
				} catch (Exception t) {
					throw new ClassLoaderException(t);
				}
			}
		}
	}	
	public Class<?> loadClassBin(String className) throws ClassNotFoundException {
		return classLoaderBin.loadClass(className);
	}
	public Class<?> loadClassLib(String className) throws ClassNotFoundException {
		return classLoaderLib.loadClass(className);
	}
	@Override
	public ClassLoader getClassLoaderBin() {
		return classLoaderBin;
	}
	@Override
	public ClassLoader getClassLoaderLib() {
		return classLoaderLib;
	}
	/**
	 * If the class was loaded previously classLoader will reload the class
	 */
	@Override
	public void installClass(String className, byte[] contents)  throws ClassLoaderException {
		if (wasInstalled(className)) {
			reloadClassLoader();
		}
		try {
			classLoaderBin.installClass(className, contents);
		} catch (Exception e) {
			throw new ClassLoaderException(e);
		}
	}
	@Override
	public void installClass(String className, byte[] clazz, boolean override) {
		classLoaderBin.installClass(className, clazz, override);
	}
	/**
	 * If you want reload a class you to must do:
	 * IClassLoaderManager.reloadClassLoader().loadClass(className);
	 * this code will affect all classes loaded for the previus ClassLoader
	 */
	@Override
	public void reloadClassLoader() {
		classLoaderBin = new ClassLoaderBin();
		//classLoaderLib = new ClassLoaderLib();
	}
	@Override
	public void uninstallClasses() {
		classLoaderBin.uninstallClasses();
	}
	@Override
	public boolean wasInstalled(String className) {
		return classLoaderBin.wasInstalled(className);
	}
	@Override
	public void reload() {
		classLoaderBin.uninstallClasses();
	}
}
