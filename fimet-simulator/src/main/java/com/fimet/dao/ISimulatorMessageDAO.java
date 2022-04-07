package com.fimet.dao;

import java.util.List;

import com.fimet.IManager;
import com.fimet.simulator.IESimulatorMessage;

public interface ISimulatorMessageDAO<T extends IESimulatorMessage> extends IManager {
	public List<T> findByModelName(String model);
	public T insert(T simulator);
	public T update(T simulator);
	public T delete(T simulator);
}
