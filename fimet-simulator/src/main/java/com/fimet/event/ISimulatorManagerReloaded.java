package com.fimet.event;

import java.util.List;

import com.fimet.simulator.ISimulator;

/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface ISimulatorManagerReloaded extends IEventListener {
	public void onSimulatorManagerReloaded(List<ISimulator> simulators);
}
