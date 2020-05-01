package com.fimet;

import java.util.List;

import com.fimet.commons.data.reader.IReader;
import com.fimet.commons.data.writer.IWriter;
import com.fimet.entity.sqlite.EFieldFormatGroup;
import com.fimet.iso8583.parser.IFieldParser;
import com.fimet.iso8583.parser.IMessage;
/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface IFieldParserManager extends IManager {
	public byte[] parse(Integer idField, IReader reader, IMessage message);
	public byte[] parse(String idField, IReader reader, IMessage message);
	public void format(IMessage message, int field, IWriter writer);
	public void format(IMessage message, String field, IWriter writer);
	public IFieldParser getFieldParser(Integer idGroup, String idField);
	public IFieldParser getFieldParser(EFieldFormatGroup group, String idField);
	public List<IFieldParser> getRootFieldParsers(EFieldFormatGroup group);
	public List<IFieldParser> getRootFieldParsers(Integer group);
	public void deleteCache(Integer idGroup);
	public Class<?>[] getParserClasses();
}
