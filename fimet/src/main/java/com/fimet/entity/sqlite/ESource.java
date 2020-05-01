package com.fimet.entity.sqlite;

import com.fimet.entity.sqlite.type.ArrayByteType;
import com.fimet.entity.sqlite.type.SourceTypeType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 *
 */
@DatabaseTable(tableName = "Source")
public class ESource {
	@DatabaseField(id = true)
	private Integer id;
	@DatabaseField(canBeNull = false)
	private String className;
	@DatabaseField(persisterClass=SourceTypeType.class, canBeNull = false)
	private ESourceType type;
	@DatabaseField(canBeNull = false)
	private String source;
	@DatabaseField(persisterClass=ArrayByteType.class, canBeNull = true)
	private byte[] bytecode;
	public ESource() {
	}
	public ESource(Integer id, String className) {
		this(id, (Integer)null, className, null, null);
	}
	public ESource(Integer idType, String className, String source, byte[] bytecode) {
		this(null, idType, className, source, bytecode);
	}
	public ESource(Integer id, Integer idType, String className, String source, byte[] bytecode) {
		super();
		this.id = id;
		this.type = idType != null ? new ESourceType(idType) : null;
		this.className = className;
		this.source = source;
		this.bytecode = bytecode;
	}
	public ESource(Integer id, ESourceType type, String className, String source, byte[] bytecode) {
		super();
		this.id = id;
		this.type = type;
		this.className = className;
		this.source = source;
		this.bytecode = bytecode;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSimpleClassName() {
		return className !=null ? className.substring(className.lastIndexOf('.')+1) : null;
	}
	public String getPackageName() {
		return className.substring(0, className.lastIndexOf('.'));
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public ESourceType getType() {
		return type;
	}
	public void setType(ESourceType type) {
		this.type = type;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public byte[] getBytecode() {
		return bytecode;
	}
	public void setBytecode(byte[] bytecode) {
		this.bytecode = bytecode;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		ESource other = (ESource) obj;
		if (id != null && id.equals(other.id)) {
			return true;
		}
		if (className == null) {
			if (other.className != null)
				return false;
		} else if (!className.equals(other.className))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return getSimpleClassName();
	}
	
}
