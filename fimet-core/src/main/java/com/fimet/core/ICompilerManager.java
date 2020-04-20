package com.fimet.core;


public interface ICompilerManager extends IManager {
	Class<?> compile(String className, String source);
}
