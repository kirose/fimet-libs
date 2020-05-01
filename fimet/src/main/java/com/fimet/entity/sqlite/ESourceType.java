package com.fimet.entity.sqlite;

import com.fimet.entity.sqlite.type.ClassType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 *
 */
@DatabaseTable(tableName = "SourceType")
public class ESourceType {
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
	public ESourceType() {
	}
	public ESourceType(Integer id) {
		this(id, null, null);
	}
	public ESourceType(String name, Class<?> clazz) {
		this(null, name, clazz);
	}
	public ESourceType(Integer id, Class<?> clazz) {
		super();
		this.id = id;
		this.name = clazz.getSimpleName();
		this.interfaceClass = clazz;
	}
	public ESourceType(Integer id,  String name, Class<?> clazz) {
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
