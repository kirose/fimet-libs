package com.fimet.event;

import java.util.List;

import com.fimet.enviroment.IEnviroment;
import com.fimet.event.IEventListener;
/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface IEnviromentManagerReloaded extends IEventListener {
	public void onEnviromentManagerReloaded(List<IEnviroment> enviroments);
}
