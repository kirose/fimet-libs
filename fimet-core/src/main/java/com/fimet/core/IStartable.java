package com.fimet.core;

/**
 * This intercafe is for used for initialize the manager
 * After allocate the new <Manager> the method instance.start() is invoked
 * see Manager: public static <T> T get(Class<T> clazz)
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface IStartable {
	public void start();
}
