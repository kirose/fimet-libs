package com.fimet.dao;

import java.util.List;

import com.fimet.IManager;
import com.fimet.parser.IEFieldFormat;

public interface IFieldFormatDAO extends IManager {
	public List<IEFieldFormat> findByGroup(String group);
	public List<IEFieldFormat> findAll();
	public IEFieldFormat insert(IEFieldFormat format);
	public IEFieldFormat update(IEFieldFormat format);
	public IEFieldFormat delete(IEFieldFormat format);
}
