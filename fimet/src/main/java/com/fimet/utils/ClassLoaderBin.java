package com.fimet.utils;

import static com.fimet.IClassLoaderManager.BIN_PATH;
import static com.fimet.IClassLoaderManager.SRC_PATH;

import java.io.File;
/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public class ClassLoaderBin extends ClassLoader {
	public ClassLoaderBin() {
		super();
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
			return Class.forName(className);
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
		} else {
			if (wasInstalled(className)) {
				throw new ClassLoaderException("The class "+className+" was installed previusly");
			} else {
				File file = new File(BIN_PATH, className.replace('.', File.separatorChar) + ".class");
				FileUtils.createSubdirectories(file);
				FileUtils.writeContents(file, clazz);
			}
		}
	}
	public void installClass(String className, byte[] clazz) {
		installClass(className, clazz, true);
	}
	public void uninstallClasses() {
		System.out.println("deleting..."+BIN_PATH);
		FileUtils.deleteFiles(BIN_PATH);
		System.out.println("deleting..."+SRC_PATH);
		FileUtils.deleteFiles(SRC_PATH);
		System.out.println("unistalled classes"+BIN_PATH);
	}
}
