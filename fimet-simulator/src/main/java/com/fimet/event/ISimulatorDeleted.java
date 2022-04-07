package com.fimet.event;

import com.fimet.simulator.IESimulator;

/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface ISimulatorDeleted extends IEventListener {
	public void onSimulatorDeleted(IESimulator s);
}
