
package com.fimet.sqlite.entity;

import java.util.HashMap;
import java.util.Map;

import com.fimet.sqlite.type.ArraySimulatorType;
import com.fimet.sqlite.type.MapStringString;
import com.fimet.sqlite.usecase.Simulator;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 *
 */
@DatabaseTable(tableName = "UseCaseStore")
public class EUseCaseStore {

	@DatabaseField(generatedId=true)
	private Long id;
	@DatabaseField(canBeNull = false)
	private String name;
	@DatabaseField(canBeNull = false)
	private Long startTime;
	@DatabaseField(canBeNull = false)
	private Long executionTime;
	@DatabaseField(canBeNull = true, persisterClass=MapStringString.class)
	private Map<String, String> data;
	@DatabaseField(persisterClass=ArraySimulatorType.class, canBeNull = true)
	private Simulator[] simulators;
	public EUseCaseStore() {
		data = new HashMap<>();
	}
	public EUseCaseStore(String name) {
		super();
		this.name = name;
		data = new HashMap<>();
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getStartTime() {
		return startTime;
	}
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
	public Long getExecutionTime() {
		return executionTime;
	}
	public void setExecutionTime(Long executionTime) {
		this.executionTime = executionTime;
	}
	public Map<String, String> getData() {
		return data;
	}
	public void setData(Map<String, String> data) {
		this.data = data;
	}
	public Simulator[] getSimulators() {
		return simulators;
	}
	public void setSimulators(Simulator[] simulators) {
		this.simulators = simulators;
	}
}
