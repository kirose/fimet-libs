package com.fimet.dao;

import java.util.List;

import com.fimet.parser.IEFieldGroup;

public interface IFieldGroupDAO {
	public IEFieldGroup getByName(String name);
	public List<IEFieldGroup> getAll();
}
