package com.fimet.utils;

import static com.fimet.Paths.LIB_PATH;

import java.io.File;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import com.fimet.FimetLogger;

/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public class ClassLoaderLib extends ClassLoader {
	private URLClassLoader urlClassLoader;
	public ClassLoaderLib() {
		super();
		newURLClassLoader();
	}
	public ClassLoaderLib(ClassLoader parent) {
		super();
		newURLClassLoader();
	}
	private URLClassLoader newURLClassLoader() {
		URL[] urls = getUrlLibraries();
		if (urls != null && urls.length > 0) {
			urlClassLoader = new URLClassLoader(
				urls,
				ClassLoaderLib.class.getClassLoader()//Permite cargas clases que solo el padre tiene acceso
			);
			return urlClassLoader;
		} else {
			return null;
		}
	} 
	private URL[] getUrlLibraries() {
		if (LIB_PATH.exists() && LIB_PATH.isDirectory()) {
			Queue<File> queue = new ArrayDeque<File>();
			queue.add(LIB_PATH);
			File dir = null;
			File[] jars;
			List<URL> urls = new ArrayList<URL>();
			while (!queue.isEmpty()) {
				dir = queue.poll();
				jars = dir.listFiles();
				if (jars != null && jars.length > 0) {
					for (File file : jars) {
						if (file.isDirectory()) {
							queue.add(file);
						} else if (file.getName().toLowerCase().endsWith(".jar")) {
							try {
								urls.add(file.toURI().toURL());
							} catch (MalformedURLException e) {
								FimetLogger.warning("Malformed URL for "+file.getAbsolutePath(),e);
							}
						}
					}
				}
			}
			if (urls.isEmpty()) {
				return null;
			} else {
				return urls.toArray(new URL[urls.size()]);
			}
		}
		return null;
	}
	@Override
	public Class<?> loadClass(String className) throws ClassNotFoundException {
		if (urlClassLoader != null) {
			return urlClassLoader.loadClass(className);
		} else {
			return Class.forName(className);
		}
	}
            
}
