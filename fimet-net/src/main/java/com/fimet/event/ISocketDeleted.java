package com.fimet.event;

import com.fimet.net.IESocket;

/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface ISocketDeleted extends IEventListener {
	public void onSocketDeleted(IESocket socket);
}
