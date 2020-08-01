package com.fimet.dao;

import java.util.List;

import com.fimet.IManager;
import com.fimet.parser.IEFieldGroup;

public interface IFieldGroupDAO extends IManager {
	public IEFieldGroup findByName(String name);
	public List<IEFieldGroup> findAll();
	public IEFieldGroup insert(IEFieldGroup group);
	public IEFieldGroup update(IEFieldGroup group);
	public IEFieldGroup delete(IEFieldGroup group);
}
