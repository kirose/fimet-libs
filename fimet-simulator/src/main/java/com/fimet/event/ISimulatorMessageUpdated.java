package com.fimet.event;

import com.fimet.simulator.IESimulatorMessage;

/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface ISimulatorMessageUpdated extends IEventListener {
	public void onSimulatorMessageUpdated(IESimulatorMessage s);
}
