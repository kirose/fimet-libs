package com.fimet.core;

import java.io.File;

/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface IClassLoaderManager extends IManager {
	
	public static final File BIN_PATH = new File("bin");
	public static final File SRC_PATH = new File("src");
	public static final File LIB_PATH = new File("lib");
	
	public boolean isInstalled(String className);
	public Class<?> loadClass(String className) throws ClassNotFoundException;
	public ClassLoader getClassLoaderBin();
	public ClassLoader getClassLoaderLib();
	public void installClass(String className, byte[] clazz, boolean override);
	public void installClass(String className, byte[] contents);
	public void reloadClassLoader();
	public void uninstallClasses();
}
