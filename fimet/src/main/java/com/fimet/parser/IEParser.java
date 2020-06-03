package com.fimet.parser;

/**
 * 
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 *
 */
public interface IEParser {
	public String getName();
	public void setName(String name);
	public void setFieldGroup(String fieldGroup);
	public String getFieldGroup();
	public String getConverter();
	public void setConverter(String converter);
	public String getParserClass();
	public void setParserClass(String parserClass);
}
