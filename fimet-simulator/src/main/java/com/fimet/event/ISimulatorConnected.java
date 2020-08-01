package com.fimet.event;

import com.fimet.simulator.ISimulator;

/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface ISimulatorConnected extends IEventListener {
	public void onSimulatorConnected(ISimulator simulator);
}
