package com.fimet.commons;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.fimet.commons.exception.FimetException;

public class FimetLogger {
	static {
		File file = new File("resources/log4j.properties");
		try {
			PropertyConfigurator.configure(new FileInputStream(file.getAbsoluteFile()));
		} catch (FileNotFoundException e) {
			throw new FimetException("Cannot found "+file.getAbsolutePath()); 
		}
	}
	static Logger logger = Logger.getLogger(FimetLogger.class);
	static int level;
	public static void debug(String message) {
		logger.debug(message);
	}
	public static void debug(Class<?> clazz, String message) {
		Logger.getLogger(clazz).debug(message);
	}
	public static void debug(Class<?> clazz, String message, Throwable e) {
		Logger.getLogger(clazz).debug(message, e);
	}
	public static void debug(String message, Throwable e) {
		logger.debug(message, e);
	}
	public static void info(Class<?> clazz, String message) {
		Logger.getLogger(clazz).info(message);
	}
	public static void info(Class<?> clazz, String message, Throwable e) {
		Logger.getLogger(clazz).info(message, e);
	}
	public static void info(String message) {
		logger.info(message);
	}
	public static void info(String message, Throwable e) {
		logger.info(message);
	}
	public static void warning(Class<?> clazz, String message) {
		Logger.getLogger(clazz).warn(message);
	}
	public static void warning(Class<?> clazz, String message, Throwable e) {
		Logger.getLogger(clazz).warn(message, e);
	}
	public static void warning(String message) {
		logger.warn(message);
	}
	public static void warning(String message, Throwable e) {
		logger.warn(message, e);
	}
	public static void error(Class<?> clazz, String message) {
		Logger.getLogger(clazz).error(message);
	}
	public static void error(Class<?> clazz, String message, Throwable e) {
		Logger.getLogger(clazz).error(message, e);
	}
	public static void error(String message) {
		logger.error(message);
	}
	public static void error(String message, Throwable e) {
		logger.error(message, e);
	}
	public static boolean isEnabledInfo() {
		return logger.isInfoEnabled();
	}
	public static boolean isEnabledDebug() {
		return logger.isDebugEnabled();
	}
	public static boolean isTraceEnabled() {
		return logger.isTraceEnabled();
	}
}
