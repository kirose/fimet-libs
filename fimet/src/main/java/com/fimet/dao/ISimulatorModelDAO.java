package com.fimet.dao;

import java.util.List;

import com.fimet.simulator.IESimulatorModel;

public interface ISimulatorModelDAO {
	public IESimulatorModel getByName(String name);
	public List<IESimulatorModel> getAll();
}
