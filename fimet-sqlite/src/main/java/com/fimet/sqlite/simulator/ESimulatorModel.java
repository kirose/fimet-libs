package com.fimet.sqlite.simulator;

import java.util.List;

import com.fimet.simulator.IESimulatorModel;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 *
 */
@DatabaseTable(tableName = "SimulatorModel")
public class ESimulatorModel implements IESimulatorModel {
	public static final char ACQUIRER = 'A';
	public static final char ISSUER = 'I';
	@DatabaseField(id = true)
	private Integer id;
	@DatabaseField(canBeNull = false)
	private String name;
	@DatabaseField(canBeNull = false)
	private char type;
	@DatabaseField(canBeNull = false)
	private String classModel;
	private List<ESimulatorMessage> simulators;
	public ESimulatorModel() {
		super();
	}
	public ESimulatorModel(Integer idSimulator, String name, char type, String classModel) {
		super();
		this.id = idSimulator;
		this.name = name;
		this.type = type;
		this.classModel = classModel;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer idSimulator) {
		this.id = idSimulator;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<ESimulatorMessage> getSimulators() {
		return simulators;
	}
	public void setSimulators(List<ESimulatorMessage> simulators) {
		this.simulators = simulators;
	}
	public char getType() {
		return type;
	}
	public void setType(char type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return name;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		//result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		ESimulatorModel other = (ESimulatorModel) obj;
		if (id != null) {
			return id.equals(other.id);
		}
		if (type != other.type) {
			return false;
		} else if (name != null)
			if (!name.equals(other.name))
				return false; 
		return true;
	}
	@Override
	public String getClassModel() {
		return classModel;
	}
	@Override
	public void setClassModel(String className) {
		this.classModel = className;
	}
}
