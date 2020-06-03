package com.fimet.sqlite.parser;


import com.fimet.parser.IEFieldFormat;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Field Format description for the fields
 * @author Marco Antonio
 *
 */
@DatabaseTable(tableName = "FieldFormat")
public class EFieldFormat implements IEFieldFormat {

	@DatabaseField(id = true)
	private Integer id;
	@DatabaseField(canBeNull = false,width=30)
	private String group;
	@DatabaseField(canBeNull = false,width=30)
	private String order;// 063.16, se utiliza para visualizar en Preferences->Parsers y para generar address de un field
	@DatabaseField(canBeNull = false,width=30)
	private String idField;// 63.EZ
	@DatabaseField(canBeNull = true,width=30)
	private String idFieldParent;// 063
	@DatabaseField(canBeNull = true)
	private String childs;//1,2,3,4,5,6,7,8,9,10,11,12
	@DatabaseField(canBeNull = false,width=30)
	private String converterValue;// NONE, HEX_TO_ASCII, ASCII_TO_HEX, EBCDIC_TO_ASCII
	@DatabaseField(canBeNull = true,width=30)
	private String converterLength;// HEX_TO_ASCII, ASCII_TO_HEX, EBCDIC_TO_ASCII
	@DatabaseField(canBeNull = true)
	private String parserLength;// Hex, HexDouble, Decimal
	@DatabaseField(canBeNull = false,width=30)
	private String mask;// [A-Z]+
	@DatabaseField(canBeNull = false)
	private Integer length;// 12, 1 numero de bytes que debe de tomar del padre
	@DatabaseField(canBeNull = true)
	private Integer maxLength;// Para campos variable
	@DatabaseField(canBeNull = false,width=70)
	private String name;// Token EZ
	@DatabaseField(canBeNull = false,width=60)
	private String classParser;	// com.fimet.fmt.impl.FieldParser

	public EFieldFormat() {}
	public EFieldFormat(String group, String order, String idField, String converterValue, String converterLength, String parserLength, String type, Integer length, Integer maxLength, String name, Class<?> classParser) {
		super();
		this.group = group;
		this.order = order;
		this.idField = idField;
		this.converterValue = converterValue;
		this.converterLength = converterLength;
		this.parserLength = parserLength;
		this.mask = type;
		this.length = length;
		this.maxLength = maxLength;
		this.name = name;
		this.classParser = classParser.getName();
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer idFieldFormat) {
		this.id = idFieldFormat;
	}
	public String getIdField() {
		return idField;
	}
	public void setIdField(String idField) {
		this.idField = idField;
	}
	public String getIdFieldParent() {
		return idFieldParent;
	}
	public void setIdFieldParent(String idFieldParent) {
		this.idFieldParent = idFieldParent;
	}
	public String getChilds() {
		return childs;
	}
	public void setChilds(String childs) {
		this.childs = childs;
	}
	public String getMask() {
		return mask;
	}
	public void setMask(String type) {
		this.mask = type;
	}
	public Integer getLength() {
		return length;
	}
	public void setLength(Integer length) {
		this.length = length;
	}
	public Integer getMaxLength() {
		return maxLength;
	}
	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getClassParser() {
		return classParser;
	}
	public void setClassParser(String classParser) {
		this.classParser = classParser;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getConverterValue() {
		return converterValue;
	}
	public void setConverterValue(String converterValue) {
		this.converterValue = converterValue;
	}
	public String getConverterLength() {
		return converterLength;
	}
	public void setConverterLength(String converterLength) {
		this.converterLength = converterLength;
	}
	public String getParserLength() {
		return parserLength;
	}
	public void setParserLength(String parserLength) {
		this.parserLength = parserLength;
	}
	@Override
	public String toString() {
		return "[id="+id +",idOrder="+order+", idField=" + idField + ", idFieldParent=" + idFieldParent + ", childs="
				+ childs + ", mask=" + mask + ", length=" + length
				+ ", name=" + name + ", classParser=" + classParser + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((order == null) ? 0 : order.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EFieldFormat other = (EFieldFormat) obj;
		/*if (idFieldFormat != null && other.idFieldFormat != null && idFieldFormat == other.idFieldFormat) {
			return true;
		}*/
		if (order == null) {
			if (other.order != null)
				return false;
		} else if (!order.equals(other.order))
			return false;
		return true;
	}
}
