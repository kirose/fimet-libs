package com.fimet.sqlite.simulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fimet.FimetException;
import com.fimet.simulator.IESimulatorMessage;
import com.fimet.simulator.SimulatorField;
import com.fimet.sqlite.type.ListSimulatorFieldType;
import com.fimet.sqlite.type.ListStringType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 *
 */
@DatabaseTable(tableName = "SimulatorMessage")
public class ESimulatorMessage implements Cloneable, IESimulatorMessage {
	@DatabaseField(id = true)
	private Integer id;
	@DatabaseField(canBeNull = false)
	private Integer idSimulator;
	@DatabaseField(canBeNull = true)
	private String header;
	@DatabaseField(canBeNull = false,width=4)
	private String mti;
	@DatabaseField(canBeNull = true)
	private String type;
	@DatabaseField(persisterClass=ListStringType.class, canBeNull = true)
	private List<String> delFields;
	@DatabaseField(persisterClass=ListSimulatorFieldType.class, canBeNull = true)
	private List<SimulatorField> addFields;
	public ESimulatorMessage() {
		super();
	}
	public ESimulatorMessage(Integer idSimulator, String header, String mti, String type, String delFields) {
		super();
		this.header = header;
		this.idSimulator = idSimulator;
		this.mti = mti;
		this.type = type;
		this.delFields = delFields == null || "".equals(delFields.trim()) ? null : Arrays.asList(delFields.split(","));
		this.addFields = new ArrayList<>();
	}
	public ESimulatorMessage(Integer idSimulator, String mti, String type, String addFields) {
		this(idSimulator, null, mti, type, addFields);
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public Integer getIdSimulator() {
		return idSimulator;
	}
	public void setIdSimulator(Integer idSimulator) {
		this.idSimulator = idSimulator;
	}
	public String getMti() {
		return mti;
	}
	public void setMti(String mti) {
		this.mti = mti;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<String> getDelFields() {
		return delFields;
	}
	public void setDelFields(List<String> delFields) {
		this.delFields = delFields;
	}
	public List<SimulatorField> getAddFields() {
		return addFields;
	}
	public void setAddFields(List<SimulatorField> addFields) {
		this.addFields = addFields;
	}
	public ESimulatorMessage addField(String key, Class<?> clazz) {
		addFields.add(new SimulatorField(key, clazz));
		return this;
	}
	public ESimulatorMessage addField(String key, String value) {
		addFields.add(new SimulatorField(key, value));
		return this;
	}
	public ESimulatorMessage clone() throws CloneNotSupportedException {
		try {
			ESimulatorMessage sm = (ESimulatorMessage) super.clone();
			if (addFields != null) {
				sm.addFields = new ArrayList<>();
				for (SimulatorField f : addFields) {
					sm.addFields.add(f.clone());	
				}
			}
			return sm;
		} catch (CloneNotSupportedException e) {
			throw new FimetException(e);
		}
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((delFields == null) ? 0 : delFields.hashCode());
		result = prime * result + ((header == null) ? 0 : header.hashCode());
		result = prime * result + ((idSimulator == null) ? 0 : idSimulator.hashCode());
		result = prime * result + ((addFields == null) ? 0 : addFields.hashCode());
		result = prime * result + ((mti == null) ? 0 : mti.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		ESimulatorMessage other = (ESimulatorMessage) obj;
		if (id != null && other.id != null && id == other.id) {
			return true;
		}
		if (delFields == null) {
			if (other.delFields != null)
				return false;
		} else if (!delFields.equals(other.delFields))
			return false;
		if (header == null) {
			if (other.header != null)
				return false;
		} else if (!header.equals(other.header))
			return false;
		if (idSimulator == null) {
			if (other.idSimulator != null)
				return false;
		} else if (!idSimulator.equals(other.idSimulator))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (addFields == null) {
			if (other.addFields != null)
				return false;
		} else if (!addFields.equals(other.addFields))
			return false;
		if (mti == null) {
			if (other.mti != null)
				return false;
		} else if (!mti.equals(other.mti))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	public boolean isRequest() {
		return type == REQUEST;
	}
	public boolean isResponse() {
		return type == RESPONSE;
	}
}
