package com.fimet.event;

import com.fimet.simulator.IESimulatorMessage;

/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface ISimulatorMessageDeleted extends IEventListener {
	public void onSimulatorMessageDeleted(IESimulatorMessage s);
}
