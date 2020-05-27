package com.fimet;

import java.io.File;

import com.fimet.utils.ClassLoaderException;

/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface IClassLoaderManager extends IManager {
	
	public static final File BIN_PATH = new File("bin");
	public static final File SRC_PATH = new File("src");
	public static final File LIB_PATH = new File("lib");
	
	public boolean wasInstalled(String className);
	public Class<?> loadClass(String className) throws ClassLoaderException;
	public ClassLoader getClassLoaderBin();
	public ClassLoader getClassLoaderLib();
	public void installClass(String className, byte[] clazz, boolean override) throws ClassLoaderException;
	public void installClass(String className, byte[] contents) throws ClassLoaderException;
	public void reloadClassLoader();
	public void uninstallClasses();
}
