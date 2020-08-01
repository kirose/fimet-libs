package com.fimet.event;

import java.util.List;

import com.fimet.socket.ISocket;

/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface ISocketManagerReloaded extends IEventListener {
	public void onSocketManagerReloaded(List<ISocket> sockets);
}
