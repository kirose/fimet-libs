package com.fimet.dao;

import java.util.List;

import com.fimet.IManager;
import com.fimet.simulator.IESimulator;

public interface ISimulatorDAO extends IManager {
	public IESimulator findByName(String name);
	public List<IESimulator> findAll();
	public IESimulator insert(IESimulator simulator);
	public IESimulator update(IESimulator simulator);
	public IESimulator delete(IESimulator simulator);
}
