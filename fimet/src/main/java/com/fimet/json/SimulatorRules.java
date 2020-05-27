package com.fimet.json;

import java.util.List;
import java.util.Map;

public class SimulatorRules {
	List<String> del;
	Map<String,String> add;
	public List<String> getDel() {
		return del;
	}
	public void setDel(List<String> del) {
		this.del = del;
	}
	public Map<String, String> getAdd() {
		return add;
	}
	public void setAdd(Map<String, String> add) {
		this.add = add;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((add == null) ? 0 : add.hashCode());
		result = prime * result + ((del == null) ? 0 : del.hashCode());
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
		SimulatorRules other = (SimulatorRules) obj;
		if (add == null) {
			if (other.add != null)
				return false;
		} else if (!add.equals(other.add))
			return false;
		if (del == null) {
			if (other.del != null)
				return false;
		} else if (!del.equals(other.del))
			return false;
		return true;
	}
}
