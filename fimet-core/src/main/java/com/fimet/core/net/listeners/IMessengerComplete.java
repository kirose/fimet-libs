package com.fimet.core.net.listeners;
/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface IMessengerComplete  extends IMessengerListener {
	/**
	 * Executed when complete the cycle correctly
	 */
	void onMessengerComplete();
}
