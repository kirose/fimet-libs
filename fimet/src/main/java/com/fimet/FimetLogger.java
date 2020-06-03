package com.fimet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
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
	public static void error(Class<?> clazz, Throwable e) {
		logger.error(clazz, e);
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
	public static boolean isWaringEnabled() {
		return logger.isDebugEnabled();
	}
	public static void setLevel(FimetLogger.Level level) {
		switch (level) {
		case OFF:
			logger.setLevel(org.apache.log4j.Level.OFF);
			break;
		case FATAL:
			logger.setLevel(org.apache.log4j.Level.FATAL);
			break;
		case ERROR:
			logger.setLevel(org.apache.log4j.Level.ERROR);
			break;
		case WARN:
			logger.setLevel(org.apache.log4j.Level.WARN);
			break;
		case INFO:
			logger.setLevel(org.apache.log4j.Level.INFO);
			break;
		case DEBUG:
			logger.setLevel(org.apache.log4j.Level.DEBUG);
			break;
		case TRACE:
			logger.setLevel(org.apache.log4j.Level.TRACE);
			break;
		case ALL:
			logger.setLevel(org.apache.log4j.Level.ALL);
			break;
		default:
			logger.setLevel(org.apache.log4j.Level.OFF);
		}
	}
	public static Level getLevel() {
		org.apache.log4j.Level level = logger.getLevel();
		switch (level.toInt()) {
		case org.apache.log4j.Level.OFF_INT:
			return Level.OFF;
		case org.apache.log4j.Level.FATAL_INT:
			return Level.FATAL;
		case org.apache.log4j.Level.ERROR_INT:
			return Level.ERROR;
		case org.apache.log4j.Level.WARN_INT:
			return Level.WARN;
		case org.apache.log4j.Level.INFO_INT:
			return Level.INFO;
		case org.apache.log4j.Level.DEBUG_INT:
			return Level.DEBUG;
		case org.apache.log4j.Level.TRACE_INT:
			return Level.TRACE;
		case org.apache.log4j.Level.ALL_INT:
			return Level.ALL;
		default:
			return Level.OFF;
		}
	}
	public enum Level {
		OFF,
		FATAL,
		ERROR,
		WARN,
		INFO,
		DEBUG,
		TRACE,
		ALL
	} 
}
