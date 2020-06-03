
package com.fimet.sqlite.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 *
 */
@DatabaseTable(tableName = "StressCycleStore")
public class EStressCycleStore {
	@DatabaseField(generatedId=true)
	private Long id;
	@DatabaseField(canBeNull = false)
	private Long numOfCycle;
	@DatabaseField(canBeNull = false)
	private String address;
	@DatabaseField(canBeNull = false)
	private Integer port;
	@DatabaseField(canBeNull = false)
	private Long numOfWrite;
	@DatabaseField(canBeNull = false)
	private Long numOfRead;
	@DatabaseField(canBeNull = false)
	private Long numOfApprovals;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getNumOfCycle() {
		return numOfCycle;
	}
	public void setNumOfCycle(Long numOfCycle) {
		this.numOfCycle = numOfCycle;
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
	public Long getNumOfApprovals() {
		return numOfApprovals;
	}
	public void setNumOfApprovals(Long numOfApprovals) {
		this.numOfApprovals = numOfApprovals;
	}
	public Long getNumOfWrite() {
		return numOfWrite;
	}
	public void setNumOfWrite(Long numOfWrite) {
		this.numOfWrite = numOfWrite;
	}
	public Long getNumOfRead() {
		return numOfRead;
	}
	public void setNumOfRead(Long numOfRead) {
		this.numOfRead = numOfRead;
	}
}
