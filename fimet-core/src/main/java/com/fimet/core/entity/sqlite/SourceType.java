package com.fimet.core.entity.sqlite;

import com.fimet.core.entity.sqlite.type.ClassType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 *
 */
@DatabaseTable(tableName = "SourceType")
public class SourceType {
	public static final int SOCKET_ADAPTER = 0;
	public static final int SOCKET_PARSER = 1;
	public static final int SOCKET_SIMULATOR = 2;
	public static final int SOCKET_ADDRESS = 3;
	@DatabaseField(id = true)
	private Integer id;
	@DatabaseField(canBeNull = false)
	private String name;
	@DatabaseField(persisterClass=ClassType.class,canBeNull = false)
	private Class<?> interfaceClass;
	public SourceType() {
	}
	public SourceType(Integer id) {
		this(id, null, null);
	}
	public SourceType(String name, Class<?> clazz) {
		this(null, name, clazz);
	}
	public SourceType(Integer id, Class<?> clazz) {
		super();
		this.id = id;
		this.name = clazz.getSimpleName();
		this.interfaceClass = clazz;
	}
	public SourceType(Integer id,  String name, Class<?> clazz) {
		super();
		this.id = id;
		this.name = name;
		this.interfaceClass = clazz;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Class<?> getInterfaceClass() {
		return interfaceClass;
	}
	public void setInterfaceClass(Class<?> interfaceClass) {
		this.interfaceClass = interfaceClass;
	}
	@Override
	public String toString() {
		return name;
	}
	
}
