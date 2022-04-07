package com.fimet.event;

import com.fimet.simulator.IESimulatorModel;

/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface ISimulatorModelInserted extends IEventListener {
	public void onSimulatorModelInserted(IESimulatorModel s);
}
