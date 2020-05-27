package com.fimet;

import java.util.List;

import com.fimet.entity.ESimulator;
import com.fimet.entity.ESimulatorMessage;
import com.fimet.simulator.ISimulatorModel;
/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface ISimulatorModelManager extends IManager {
	
	public List<ESimulator> getEntities();
	public List<ESimulator> getAcquirerEntities();
	public List<ESimulator> getIssuerEntities();
	public ESimulator getEntity(Integer id);
	public ESimulator getEntity(String name);
	public ESimulator saveSimulator(ESimulator simulator);
	public ESimulator deleteSimulator(ESimulator simulator);
	public ESimulatorMessage saveSimulatorMessage(ESimulatorMessage message);
	public ESimulatorMessage deleteSimulatorMessage(ESimulatorMessage message);
	public Integer getNextIdSimulator();
	public Integer getPrevIdSimulator();
	public ISimulatorModel getSimulatorModel(String name);
	public void reloadSimulator(String name);
	public void reloadSimulators(List<String> names);
}
