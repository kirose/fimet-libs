package com.fimet;

import com.fimet.simulator.ISimulator;
import com.fimet.simulator.ISimulatorStore;
import com.fimet.simulator.ISimulatorThread;
import com.fimet.simulator.PSimulator;
/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface ISimulatorManager extends IManager {
	public ISimulator getSimulator(String externalId);
	public ISimulator getSimulator(PSimulator simulator);
	public ISimulator connect(PSimulator simulator);
	public void disconnect(PSimulator simulator);
	public void disconnectAll();
	public ISimulatorThread getNextSimulatorThread();
	public void setStore(ISimulatorStore store);
	public Class<?>[] getSimulatorFieldClasses();
}
