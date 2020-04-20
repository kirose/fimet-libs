package com.fimet.core.net.listeners;

import com.fimet.core.net.IMessenger;
/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface IMessengerDisconnected extends IMessengerListener {
	void onMessengerDisconnected(IMessenger conn);
}
