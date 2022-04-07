package com.fimet.event;

import com.fimet.net.ISocket;

/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface ISocketDisconnected extends IEventListener {
	public void onSocketDisconnected(ISocket socket);
}
