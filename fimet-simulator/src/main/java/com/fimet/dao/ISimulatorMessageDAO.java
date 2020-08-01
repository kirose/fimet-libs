package com.fimet.dao;

import java.util.List;

import com.fimet.IManager;
import com.fimet.simulator.IESimulatorMessage;

public interface ISimulatorMessageDAO extends IManager {
	public List<IESimulatorMessage> findByModelName(String model);
	public IESimulatorMessage insert(IESimulatorMessage simulator);
	public IESimulatorMessage update(IESimulatorMessage simulator);
	public IESimulatorMessage delete(IESimulatorMessage simulator);
}
