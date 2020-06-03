package com.fimet.dao;

import java.util.List;

import com.fimet.simulator.IESimulatorMessage;

public interface ISimulatorMessageDAO {
	public List<IESimulatorMessage> getByModelName(String model);
}
