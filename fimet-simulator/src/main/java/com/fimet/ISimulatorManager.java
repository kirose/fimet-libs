package com.fimet;

import java.util.List;

import com.fimet.simulator.IESimulator;
import com.fimet.simulator.ISimulator;
import com.fimet.simulator.ISimulatorStore;
/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface ISimulatorManager extends IManager {
	public static final String PACKAGE_EXTENSIONS = "com.fimet.simulator.extension";
	public ISimulator getSimulator(String name);
	public ISimulator getSimulator(IESimulator simulator);
	public ISimulator connect(IESimulator simulator);
	public boolean disconnect(String name);
	public void disconnectAll();
	public ISimulator remove(String name);
	public ISimulator reload(String name);
	public List<ISimulator> getSimulators();
	public void setStore(ISimulatorStore store);
	public boolean connect(String name);
	public void connectAll();
}
