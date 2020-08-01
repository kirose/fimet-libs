package com.fimet.event;

import com.fimet.socket.IESocket;

/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface ISocketUpdated extends IEventListener {
	public void onSocketUpdated(IESocket socket);
}
