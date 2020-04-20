package com.fimet.core.net.listeners;

import com.fimet.core.net.IMessenger;
/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface IMessengerConnected extends IMessengerListener {
	void onMessengerConnected(IMessenger conn);
}
