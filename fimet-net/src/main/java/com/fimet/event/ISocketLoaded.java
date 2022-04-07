package com.fimet.event;

import com.fimet.net.ISocket;

/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface ISocketLoaded extends IEventListener {
	public void onSocketLoaded(ISocket s);
}
