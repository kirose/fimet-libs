package com.fimet.dao;

import java.util.List;

import com.fimet.simulator.IESimulator;

public interface ISimulatorDAO {
	public IESimulator getByName(String name);
	public List<IESimulator> getAll();
}
