package com.fimet.dao;

import java.util.List;

import com.fimet.parser.IEFieldFormat;

public interface IFieldFormatDAO {
	public List<IEFieldFormat> getByGroup(String group);
	public List<IEFieldFormat> getAll();
}
