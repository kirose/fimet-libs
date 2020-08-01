package com.fimet.event;

import com.fimet.enviroment.IEEnviroment;
import com.fimet.event.IEventListener;
/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface IEnviromentInserted extends IEventListener {
	public void onEnviromentInserted(IEEnviroment e);
}
