
package com.fimet.entity;

import com.fimet.parser.IMessage;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 *
 */
@DatabaseTable(tableName = "SimulatorStore")
public class ESimulatorStore {
	@DatabaseField(generatedId=true)
	private Long id;
	@DatabaseField(canBeNull = false)
	private String address;
	@DatabaseField(canBeNull = false)
	private Integer port;
	@DatabaseField(canBeNull = false)
	private String model;
	@DatabaseField(canBeNull = false)
	private Long time;
	@DatabaseField(canBeNull = false)
	private Character type;
	@DatabaseField(canBeNull = false)
	private IMessage message;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public IMessage getMessage() {
		return message;
	}
	public void setMessage(IMessage message) {
		this.message = message;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public Character getType() {
		return type;
	}
	public void setType(Character type) {
		this.type = type;
	}
}
