package com.fimet;

import static com.fimet.Paths.SRC_PATH;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import com.fimet.utils.CompilationException;
import com.fimet.utils.FileUtils;

public class CompilerManager extends AbstractManager implements ICompilerManager {
	private IClassLoaderManager classLoaderManager = Manager.get(IClassLoaderManager.class);
	public CompilerManager() {
		String javaHome = Manager.getProperty("java.home");
		if (javaHome != null) {
			FimetLogger.debug(CompilerManager.class, "Change java.home"
				+"\n\tFrom:"+System.getProperty("java.home")
				+"\n\tTo:"+javaHome
				+"java.class.path:\n"+System.getProperty("java.class.path")
			);
			System.setProperty("java.home", javaHome);
		}
	}
	@Override
	public Class<?> compileAndReload(String className, String source) {
		compileAndInstall(className, source);
		return classLoaderManager.reloadClass(className);
	}
	@Override
	public Class<?> compileAndLoad(String className, String source) {
		compileAndInstall(className, source);
		return classLoaderManager.loadClass(className);
	}
	@Override
	public String compile(String className, String source) {
		return compileAndInstall(className, source);
	}
	private String compileAndInstall(String className, String source) {
		try {
			File sourceFile = new File(SRC_PATH, className.replace('.', File.separatorChar)+".java");
			File classFile = new File(SRC_PATH, className.replace('.', File.separatorChar)+".class");
			sourceFile.getParentFile().mkdirs();
			Files.write(sourceFile.toPath(), source.getBytes(StandardCharsets.UTF_8));
	
			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			if (compiler == null) {
				throw new CompilationException("Cannot found compiler, you must set a valid jdk route for java.home property see fimet.xml");
			}
			ByteArrayOutputStream streamErr = new ByteArrayOutputStream();
			//String javaCP = System.getProperty("java.class.path")+File.separatorChar+"SomeJarPath";
			//int status = compiler.run(null, null, streamErr, new String[] {sourceFile.getPath(),"-classpath",javaCP});
			int status = compiler.run(null, null, streamErr, sourceFile.getPath());
			if (status != 0) {
				String errors = new String(streamErr.toByteArray());
				if (errors != null && errors.length() > 0) {
					FimetLogger.error(CompilerManager.class, "Compilation error:\n"+errors);
				}
				throw new CompilationException("Compilation error for class "+className+"\n see log for complete errors:\n"+errors.substring(0,Math.min(100, errors.length())));
			} else {
				FimetLogger.debug(CompilerManager.class,"Class compiled: "+className);
				byte[] contents = FileUtils.readBytesContents(classFile);
				FileUtils.delete(classFile);
				classLoaderManager.installClass(className, contents, true);
				return className;
			}
		} catch (Exception e) {
			if (e instanceof CompilationException) {
				throw ((CompilationException)e);
			}
			throw new CompilationException("Compilation error for class "+className);
		}
	}
}
