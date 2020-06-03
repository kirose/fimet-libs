package com.fimet.parser;



/**
 * Field Format description for the fields
 * @author Marco Antonio
 *
 */
public interface IEFieldFormat {
	public String getIdField();
	public void setIdField(String idField);
	public String getGroup();
	public void setGroup(String group);
	public String getIdFieldParent();
	public void setIdFieldParent(String idFieldParent);
	public String getChilds();
	public void setChilds(String childs);
	public String getConverterValue();
	public void setConverterValue(String converterValue);
	public String getConverterLength();
	public void setConverterLength(String converterLength);
	public String getParserLength();
	public void setParserLength(String parserLength);
	public String getMask();
	public void setMask(String type);
	public Integer getLength();
	public void setLength(Integer length);
	public Integer getMaxLength();
	public void setMaxLength(Integer maxLength);
	public String getName();
	public void setName(String name);
	public String getClassParser();
	public void setClassParser(String classParser);
	public String getOrder();
	public void setOrder(String order);
}
