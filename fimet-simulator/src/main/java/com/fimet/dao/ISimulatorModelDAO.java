package com.fimet.dao;

import java.util.List;

import com.fimet.IManager;
import com.fimet.simulator.IESimulatorModel;

public interface ISimulatorModelDAO<T extends IESimulatorModel> extends IManager {
	public T findByName(String name);
	public List<T> findAll();
	public T insert(T simulator);
	public T update(T simulator);
	public T delete(T simulator);
}
