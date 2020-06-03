package com.fimet.sqlite.parser;


import com.fimet.parser.IEFieldGroup;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Field Format description for the fields
 * @author Marco Antonio
 *
 */
@DatabaseTable(tableName = "FieldGroup")
public class EFieldGroup implements IEFieldGroup {

	@DatabaseField(id = true)
	private Integer id;
	@DatabaseField(canBeNull = false)
	private String name;
	@DatabaseField(canBeNull = false)
	private String description;
	@DatabaseField(canBeNull = true)
	private String parent;


	public EFieldGroup() {}
	public EFieldGroup(Integer id, String name, String description) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
	}
	public EFieldGroup(Integer id, String name, String description, String parent) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.parent = parent;
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
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return name;
	}
}
