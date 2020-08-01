package com.fimet.utils;

import static com.fimet.Paths.BIN_PATH;
import static com.fimet.Paths.SRC_PATH;

import java.io.File;

import com.fimet.FimetLogger;
/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public class ClassLoaderBin extends ClassLoader {
	public ClassLoaderBin() {
		super(ClassLoaderBin.class.getClassLoader());
	}
	public ClassLoaderBin(ClassLoader parent) {
		super(parent);
	}
	@Override
    public Class<?> findClass(String className) throws ClassNotFoundException {
		File path = new File(BIN_PATH, className.replace('.', File.separatorChar) + ".class");
		if (path.exists()) {
			byte[] contents = FileUtils.readBytesContents(path);
			return defineClass(className, contents, 0, contents.length);
		} else {
			//return PluginUtils.loadClass(className);
			return getParent().loadClass(className);
		}
	}
	public boolean wasInstalled(String className) {
		return new File(BIN_PATH, className.replace('.', File.separatorChar) + ".class").exists();
	}
	public void installClass(String className, byte[] clazz, boolean override) throws ClassLoaderException {
		if (override) {
			File file = new File(BIN_PATH, className.replace('.', File.separatorChar) + ".class");
			FileUtils.createSubdirectories(file);
			FileUtils.writeContents(file, clazz);
			FimetLogger.debug(ClassLoaderBin.class,"Class installed: "+className);
		} else {
			if (wasInstalled(className)) {
				throw new ClassLoaderException("The class "+className+" was installed previusly");
			} else {
				File file = new File(BIN_PATH, className.replace('.', File.separatorChar) + ".class");
				FileUtils.createSubdirectories(file);
				FileUtils.writeContents(file, clazz);
				FimetLogger.debug(ClassLoaderBin.class,"Class installed: "+className);
			}
		}
	}
	public void installClass(String className, byte[] clazz) {
		installClass(className, clazz, true);
	}
	public void uninstallClasses() {
		FimetLogger.debug(ClassLoaderBin.class, "deleting..."+BIN_PATH);
		FileUtils.deleteFiles(BIN_PATH);
		FimetLogger.debug(ClassLoaderBin.class, "deleting..."+SRC_PATH);
		FileUtils.deleteFiles(SRC_PATH);
		FimetLogger.debug(ClassLoaderBin.class, "unistalled classes"+BIN_PATH);
	}
}
