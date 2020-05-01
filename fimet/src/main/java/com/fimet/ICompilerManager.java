package com.fimet;


public interface ICompilerManager extends IManager {
	Class<?> compile(String className, String source);
}
