package com.fimet;

import java.util.List;

import com.fimet.simulator.ISimulatorModel;
/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface ISimulatorModelManager extends IManager {
	public ISimulatorModel getSimulatorModel(String name);
	public List<String> getNames();
	public void reload(String name);
	public Class<?>[] getSimulatorFieldClasses();
}
