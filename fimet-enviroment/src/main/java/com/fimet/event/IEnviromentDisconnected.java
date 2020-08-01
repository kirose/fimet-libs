package com.fimet.event;

import com.fimet.enviroment.IEnviroment;
import com.fimet.event.IEventListener;
/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface IEnviromentDisconnected extends IEventListener {
	public void onEnviromentDisconnected(IEnviroment e);
}