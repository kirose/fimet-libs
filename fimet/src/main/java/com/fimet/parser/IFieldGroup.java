package com.fimet.parser;

import java.util.List;

import com.fimet.utils.IReader;
import com.fimet.utils.IWriter;

public interface IFieldGroup {
	public String getName();
	public byte[] parse(String idField, IMessage message, IReader reader);
	public byte[] parse(int idField, IMessage message, IReader reader);
	public void format(String idField, IMessage message, IWriter writer);
	public void format(int idField, IMessage message, IWriter writer);
	public short[] getAddress(String idField);
	public List<IFieldParser> getRoots();
	public IFieldParser getFieldParser(String idFieldParent);
}
