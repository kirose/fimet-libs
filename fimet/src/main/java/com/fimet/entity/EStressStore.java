
package com.fimet.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 *
 */
@DatabaseTable(tableName = "StressStore")
public class EStressStore {
	@DatabaseField(generatedId=true)
	private Long id;
	@DatabaseField(canBeNull = false)
	private String name;
	@DatabaseField(canBeNull = false)
	private String address;
	@DatabaseField(canBeNull = false)
	private Integer port;
	@DatabaseField(canBeNull = false)
	private Long time;
	@DatabaseField(canBeNull = false)
	private Long cycleTime;
	@DatabaseField(canBeNull = false)
	private Long cycles;
	@DatabaseField(canBeNull = false)
	private Long numOfApprovals;
	@DatabaseField(canBeNull = false)
	private Long numOfWrite;
	@DatabaseField(canBeNull = false)
	private Long numOfRead;
	@DatabaseField(canBeNull = false)
	private Long numOfInjec;
	@DatabaseField(canBeNull = false)
	private Long injectStartTime;
	@DatabaseField(canBeNull = false)
	private Long injectFinishTime;
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
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public Long getCycleTime() {
		return cycleTime;
	}
	public void setCycleTime(Long cycleTime) {
		this.cycleTime = cycleTime;
	}
	public Long getCycles() {
		return cycles;
	}
	public void setCycles(Long cycles) {
		this.cycles = cycles;
	}
	public void setNumOfApprovals(Long numOfApprovals) {
		this.numOfApprovals = numOfApprovals;
	}
	public Long getNumOfApprovals() {
		return numOfApprovals;
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
	public Long getNumOfInjec() {
		return numOfInjec;
	}
	public void setNumOfInjec(Long numOfInjec) {
		this.numOfInjec = numOfInjec;
	}
	public Long getInjectStartTime() {
		return injectStartTime;
	}
	public void setInjectStartTime(Long injectStartTime) {
		this.injectStartTime = injectStartTime;
	}
	public Long getInjectFinishTime() {
		return injectFinishTime;
	}
	public void setInjectFinishTime(Long injectFinishTime) {
		this.injectFinishTime = injectFinishTime;
	}
}
