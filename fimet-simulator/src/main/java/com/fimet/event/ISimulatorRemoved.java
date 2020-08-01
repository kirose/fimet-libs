package com.fimet.event;

import com.fimet.simulator.ISimulator;

/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface ISimulatorRemoved extends IEventListener {
	public void onSimulatorRemoved(ISimulator simulator);
}
