package com.fimet.dao;

import java.util.List;

import com.fimet.IManager;
import com.fimet.simulator.IESimulatorModel;

public interface ISimulatorModelDAO extends IManager {
	public IESimulatorModel findByName(String name);
	public List<IESimulatorModel> findAll();
	public IESimulatorModel insert(IESimulatorModel simulator);
	public IESimulatorModel update(IESimulatorModel simulator);
	public IESimulatorModel delete(IESimulatorModel simulator);
}
